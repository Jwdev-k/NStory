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
        try (FileOutputStream fileOutputStream = new FileOutputStream(videoFile)) { // true: append 모드
            fileOutputStream.write(mediaData); // 영상 데이터 추가

            CompletableFuture.runAsync(() -> {
                String m3u8File = roomDir + File.separator + "NSLive.m3u8";
                if (!Files.exists(Path.of(m3u8File))) {
                    startHlsProcess(roomId, videoFile.getAbsolutePath(), m3u8File);
                } else {
                    appendToHlsProcess(roomId, videoFile.getAbsolutePath(), m3u8File);
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

    private void startHlsProcess(String roomId, String inputFilePath, String hlsPath) {
        String ffmpegCommand = String.format("ffmpeg -re -i %s -c:v libx264 -preset veryfast -c:a aac -b:a 48k " +
                        "-hls_time 3 -hls_list_size 4 -hls_delete_threshold 1 " +
                        "-hls_flags delete_segments+omit_endlist+append_list " +
                        "-hls_segment_filename %s-%%d.ts -f hls %s",
                inputFilePath, inputFilePath.replace(".webm", ""), hlsPath);
        ProcessBuilder processBuilder = new ProcessBuilder();
        //processBuilder.command("bash", "-c", ffmpegCommand); // Linux/macOS에서는 bash를 사용
        processBuilder.command("cmd.exe", "/c", ffmpegCommand); // Windows에서는 cmd를 사용
        processBuilder.directory(Path.of(".").toAbsolutePath().normalize().toFile());
        try {
            Process process = processBuilder.start();
            hlsProcess.putIfAbsent(roomId, process); // 프로세스 목록 저장

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

    private void appendToHlsProcess(String roomId, String inputFilePath , String hlsPath) {
        Process process = hlsProcess.get(roomId);
        if (process != null && process.isAlive()) {
            try (OutputStream stdin = process.getOutputStream()) {
                Files.copy(Path.of(inputFilePath), stdin);
            } catch (IOException e) {
                throw new RuntimeException("Failed to append to HLS process", e);
            } finally {
                deleteFile(inputFilePath);
            }
        } else {
            log.warn("Not Found HLS Process");
            startHlsProcess(roomId, inputFilePath, hlsPath);
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
