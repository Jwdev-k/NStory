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
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScreenShareHandler extends BinaryWebSocketHandler {
    private static final ChatRoomService chatRoomService = new ChatRoomService();

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

        // liveVideos 폴더에 roomId.m3u8 파일로 영상 데이터 저장 또는 추가
        int crtTimes = (int) System.currentTimeMillis();
        File videoFile = new File(roomDir + File.separator + roomId + crtTimes + ".webm");
        try (FileOutputStream fileOutputStream = new FileOutputStream(videoFile)) { // true: append 모드
            fileOutputStream.write(mediaData); // 영상 데이터 추가
            CompletableFuture.runAsync(() -> {
                String m3u8File = roomDir + File.separator + roomId + ".m3u8";
                if (!Files.exists(Path.of(m3u8File))) {
                    convertToM3U8(videoFile.getAbsolutePath(), m3u8File);
                } else {
                    appendToM3U8(videoFile.getAbsolutePath(), m3u8File);
                }
            });
            for (WebSocketSession s : sessionList) {
                if (s.isOpen()) {
                    CompletableFuture.runAsync(() -> {
                        try {
                            s.sendMessage(new TextMessage("Updated Video"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }

    private void convertToM3U8(String inputFilePath, String outputM3U8Path) {
        String ffmpegCommand = String.format("ffmpeg -i %s -profile:v baseline -level 3.0 -s 1920x1080 -start_number 0 -hls_time 10 -hls_list_size 0 -f hls %s",
                inputFilePath, outputM3U8Path);

        ProcessBuilder processBuilder = new ProcessBuilder();
        //processBuilder.command("bash", "-c", ffmpegCommand); // Linux/macOS에서는 bash를 사용
        processBuilder.command("cmd.exe", "/c", ffmpegCommand); // Windows에서는 cmd를 사용
        processBuilder.directory(Path.of(".").toAbsolutePath().normalize().toFile());

        try {
            Process process = processBuilder.start();

            // 에러 스트림 처리
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.warn(line);
                }
            }

            // FFmpeg 프로세스 완료까지 대기
            boolean completed = process.waitFor(1, TimeUnit.MINUTES);
            if (!completed) {
                process.destroy();
                throw new RuntimeException("FFmpeg process timeout");
            }

            // FFmpeg 실행 결과 확인
            int exitValue = process.exitValue();
            if (exitValue != 0) {
                throw new RuntimeException("FFmpeg process failed with exit code: " + exitValue);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to convert video to M3U8", e);
        }
    }

    private void appendToM3U8(String inputFilePath, String outputM3U8Path) {
        // TS 파일 생성
        String tsFilePath = inputFilePath.replace(".webm", ".ts");
        convertToTS(inputFilePath, tsFilePath);

        try {
            // 기존 M3U8 파일 열기
            Path path = Paths.get(outputM3U8Path);
            List<String> lines = Files.readAllLines(path);

            // TS 스트림 정보 추가
            lines.add("#EXTINF:10.0,");
            String tsName = new File(tsFilePath).getName();
            lines.add(tsName);

            // 기존 M3U8 파일 업데이트
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Failed to append TS stream to M3U8 file", e);
        }
    }

    private void convertToTS(String inputFilePath, String outputTSPath) {
        // FFmpeg를 사용하여 WebM을 TS로 변환
        String ffmpegCommand = String.format("ffmpeg -i %s -c:v libx264 -c:a aac %s", inputFilePath, outputTSPath);

        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("cmd.exe", "/c", ffmpegCommand); // Windows에서는 cmd를 사용
        processBuilder.directory(Path.of(".").toAbsolutePath().normalize().toFile());

        try {
            Process process = processBuilder.start();

            // 에러 스트림 처리
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.warn(line);
                }
            }

            // FFmpeg 프로세스 완료까지 대기
            boolean completed = process.waitFor(1, TimeUnit.MINUTES);
            if (!completed) {
                process.destroy();
                throw new RuntimeException("FFmpeg process timeout");
            }

            // FFmpeg 실행 결과 확인
            int exitValue = process.exitValue();
            if (exitValue != 0) {
                throw new RuntimeException("FFmpeg process failed with exit code: " + exitValue);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to convert video to M3U8", e);
        }
    }
}
