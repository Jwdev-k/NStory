package nk.service.NStory.handler;

import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.service.liveChat.ChatRoomService;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScreenShareHandler extends BinaryWebSocketHandler {
    private static final ChatRoomService chatRoomService = new ChatRoomService();
    private static final ConcurrentHashMap<String, Process> hlsProcess = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 세션의 uri 에서 roomId 가져오기
        String[] path = session.getUri().toString().split("/");
        String roomId = path[path.length - 1];

        if (roomId != null && session.isOpen()) {
            chatRoomService.joinLiveShare(roomId, session);
        } else {
            log.error("roomId is null!");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 세션의 uri 에서 roomId 가져오기
        String[] path = session.getUri().toString().split("/");
        String roomId = path[path.length - 1];

        chatRoomService.leaveLiveShare(roomId, session);

        // FFmpeg 프로세스 완료까지 대기
        Process process = hlsProcess.get(roomId);
        boolean completed = process.waitFor(1, TimeUnit.MINUTES);
        if (!completed) {
            process.destroy();
            hlsProcess.remove(roomId);
            throw new RuntimeException("FFmpeg Process End.");
        }
        session.close();
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        log.info("영상 데이터 보냄 길이: " + message.getPayloadLength());

        // 세션의 uri 에서 roomId 가져오기
        String[] path = session.getUri().toString().split("/");
        String roomId = path[path.length - 1];

        List<WebSocketSession> sessionList = chatRoomService.getRoom(roomId).getLiveSessionList();

        byte[] mediaData = message.getPayload().array();

        String baseDir = System.getProperty("user.dir") + File.separator + "liveVideos";
        String roomDir = baseDir + File.separator + roomId;

        // roomId 이름의 폴더가 존재하지 않으면 폴더 생성
        File directory = new File(roomDir);
        if (!directory.exists()) {
            directory.mkdirs(); // 중첩된 디렉터리를 생성
        }

        File videoFile = new File(roomDir + File.separator + new Date().getTime() + ".webm");
        try (FileOutputStream fileOutputStream = new FileOutputStream(videoFile)) {
            fileOutputStream.write(mediaData); // 영상 데이터 추가

            CompletableFuture.runAsync(() -> {
                String masterPlaylist = roomDir + File.separator + "NSLive.m3u8";
                if (!Files.exists(Path.of(masterPlaylist))) {
                    startHlsProcess(roomId, videoFile.getAbsolutePath(), roomDir);
                } else {
                    appendToHlsProcess(roomId, videoFile.getAbsolutePath(), roomDir);
                }

                for (WebSocketSession s : sessionList) {
                    if (s.isOpen()) {
                        try {
                            s.sendMessage(new TextMessage("Updated Video"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void startHlsProcess(String roomId, String inputFilePath, String roomDir) {
        String baseFileName = roomDir + File.separator + "NSLive";

        String ffmpegCommand = String.format(
                "ffmpeg -re -i %s " +
                        // 480p 해상도 스트림
                        "-vf scale=w=854:h=480 -c:v libx264 -preset veryfast -c:a aac -b:a 48k " +
                        "-hls_time 3 -hls_list_size 4 -hls_delete_threshold 1 " +
                        "-hls_segment_type fmp4 -hls_flags delete_segments+omit_endlist+append_list " +
                        "-hls_segment_filename %s-480-%%d.m4s -hls_fmp4_init_filename %s-480-init.mp4 -init_seg_name %s-480-init.mp4 -f hls %s-480.m3u8 " +
                        // 720p 해상도 스트림
                        "-vf scale=w=1280:h=720 -c:v libx264 -preset veryfast -c:a aac -b:a 48k " +
                        "-hls_time 3 -hls_list_size 4 -hls_delete_threshold 1 " +
                        "-hls_segment_type fmp4 -hls_flags delete_segments+omit_endlist+append_list " +
                        "-hls_segment_filename %s-720-%%d.m4s -hls_fmp4_init_filename %s-720-init.mp4 -init_seg_name %s-720-init.mp4 -f hls %s-720.m3u8 " +
                        // 1080p 해상도 스트림
                        "-vf scale=w=1920:h=1080 -c:v libx264 -preset veryfast -c:a aac -b:a 48k " +
                        "-hls_time 3 -hls_list_size 4 -hls_delete_threshold 1 " +
                        "-hls_segment_type fmp4 -hls_flags delete_segments+omit_endlist+append_list " +
                        "-hls_segment_filename %s-1080-%%d.m4s -hls_fmp4_init_filename %s-1080-init.mp4 -init_seg_name %s-1080-init.mp4 -f hls %s-1080.m3u8",
                inputFilePath, baseFileName, baseFileName, baseFileName,
                baseFileName, baseFileName, baseFileName
        );

        ProcessBuilder processBuilder = new ProcessBuilder();
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            processBuilder.command("cmd.exe", "/c", ffmpegCommand);
        } else {
            processBuilder.command("bash", "-c", ffmpegCommand);
        }

        processBuilder.directory(Path.of(".").toAbsolutePath().normalize().toFile());
        try {
            Process process = processBuilder.start();
            hlsProcess.putIfAbsent(roomId, process); // 프로세스 저장

            // 마스터 플레이리스트 생성
            createMasterPlaylist(roomDir);

            // 에러 스트림 처리
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.warn(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert video to M3U8", e);
        } finally {
            deleteFile(inputFilePath);
        }
    }

    private void createMasterPlaylist(String roomDir) {
        String masterPlaylistPath = roomDir + File.separator + "NSLive.m3u8";
        String content = "#EXTM3U\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=800000,RESOLUTION=854x480\n" +
                "NSLive-480.m3u8\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=1400000,RESOLUTION=1280x720\n" +
                "NSLive-720.m3u8\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=2800000,RESOLUTION=1920x1080\n" +
                "NSLive-1080.m3u8\n";

        try {
            Files.writeString(Path.of(masterPlaylistPath), content);
            log.info("Created master playlist: " + masterPlaylistPath);
        } catch (IOException e) {
            log.error("Failed to create master playlist", e);
        }
    }

    private void appendToHlsProcess(String roomId, String inputFilePath, String roomDir) {
        Process process = hlsProcess.get(roomId);
        if (process != null && process.isAlive()) {
            try (OutputStream stdin = process.getOutputStream()) {
                Files.copy(Path.of(inputFilePath), stdin); // 기존 프로세스에 데이터 추가
            } catch (IOException e) {
                throw new RuntimeException("Failed to append to HLS process", e);
            } finally {
                deleteFile(inputFilePath);
            }
        } else {
            log.warn("Not Found HLS Process");
            startHlsProcess(roomId, inputFilePath, roomDir);
        }
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                log.info("Deleted file: " + filePath);
            } else {
                log.warn("Failed to delete file: " + filePath);
            }
        }
    }
}
