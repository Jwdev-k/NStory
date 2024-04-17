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
        int segmentNumber = 0;  // 세그먼트 번호를 저장할 변수
        // 동일한 파일명의 .webm 파일이 있는지 확인
        while (true) {
            File tsVideoFile = new File(roomDir + File.separator + "live" + "-" + segmentNumber + ".ts");
            if (!tsVideoFile.exists()) {
                break; // 동일한 파일명의 .ts 파일이 없으면 while 문 종료
            }
            segmentNumber++; // 세그먼트 번호 증가
        }
        File videoFile = new File(roomDir + File.separator + "live" + "-" + segmentNumber + ".webm");
        try (FileOutputStream fileOutputStream = new FileOutputStream(videoFile)) { // true: append 모드
            fileOutputStream.write(mediaData); // 영상 데이터 추가

            CompletableFuture.runAsync(() -> {
                String m3u8File = roomDir + File.separator + "NSLive" + ".tmp.m3u8";
                if (!Files.exists(Path.of(m3u8File))) {
                    convertToM3U8(videoFile.getAbsolutePath(), m3u8File);
                } else {
                    // TS 파일 생성
                    String tsFilePath = videoFile.getAbsolutePath().replace(".webm", ".ts");
                    convertToTS(videoFile.getAbsolutePath(), tsFilePath);
                    appendToM3U8(tsFilePath, m3u8File);
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

    private void convertToM3U8(String inputFilePath, String outputM3U8Path) {
        String ffmpegCommand = String.format("ffmpeg -i %s -s 1280x720 -c:v libx264 -preset veryfast -b:v 1000K -c:a aac -strict experimental -start_number 0 -hls_time 4 -hls_list_size 0 -hls_flags omit_endlist -hls_segment_filename %s-%%d.ts -f hls %s",
                inputFilePath, inputFilePath.replace(".webm", "").replace("-0", ""), outputM3U8Path);

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

            process.destroy();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to convert video to M3U8", e);
        } finally {
            deleteFile(inputFilePath);
        }
    }

    private void appendToM3U8(String tsFilePath, String outputM3U8Path) {
        try {
            // 기존 M3U8 파일 열기
            Path path = Paths.get(outputM3U8Path);
            List<String> oldLines = Files.readAllLines(path);

            int segmentCount = 0;

            for (String line : oldLines) {
                if (line.startsWith("#EXTINF")) { // 세그먼트 정보만 포함된 라인
                    segmentCount++;
                }
            }

            if(segmentCount > 2) {
                oldLines.set(2, "#EXT-X-TARGETDURATION:3");

                Path newPath = Path.of(path.toString().replace(".tmp", ""));
                if (!Files.exists(newPath)) {
                    Files.copy(path, newPath); // tmp에서 스트림용 m3u8 생성
                } else {
                    // EXT-X-MEDIA-SEQUENCE 값 증가
                    int mediaSequence = Integer.parseInt(oldLines.get(3).split(":")[1]);
                    oldLines.set(3, "#EXT-X-MEDIA-SEQUENCE:" + (mediaSequence + 1));

                    Files.write(newPath, oldLines);
                }

/*                // 세그먼트만 m3u8에서 초기화 시켜서 새로운 m3u8 tmp 생성
                for (String line : oldLines) {
                    if (!line.startsWith("#EXTINF")) { // 세그먼트 정보만 포함된 라인
                        if (!line.contains(".ts")) {
                            newTmp.add(line);
                        }
                    }
                }*/

                // m3u8 원본 업데이트 후 tmp에서 세그먼트 1개 삭제
                oldLines.remove(4);
                oldLines.remove(4);

                // 새로운 내용을 파일에 쓰기
                Files.write(path, oldLines);
            }

            List<String> newLines = Files.readAllLines(path); //m3u8 세그먼트 초기화 후 파일 다시 읽어들임

            // TS 스트림 정보 추가
            long tsDuration = getDurationOfTS(tsFilePath);
            newLines.add(String.format("#EXTINF:%.1f,", (float) tsDuration / 1000));  // milliseconds to seconds
            String tsName = new File(tsFilePath).getName();
            newLines.add(tsName);
            // tmp m3u8 세그먼트 추가
            Files.write(path, newLines);
        } catch (IOException e) {
            throw new RuntimeException("Failed to append TS stream to M3U8 file", e);
        }
    }

    private void convertToTS(String inputFilePath, String outputTSPath) {
        // FFmpeg를 사용하여 WebM을 TS로 변환
        String ffmpegCommand = String.format("ffmpeg -i %s -s 1280x720 -c:v libx264 -b:v 1000K -preset veryfast -c:a aac -strict experimental %s",
                inputFilePath, outputTSPath);

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

            process.destroy();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to convert video to M3U8", e);
        } finally {
            deleteFile(inputFilePath);
        }
    }

    private long getDurationOfTS(String tsFilePath) throws IOException {
        String ffprobeCommand = String.format("ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 %s", tsFilePath);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", ffprobeCommand); // Windows에서는 cmd를 사용
        processBuilder.directory(Path.of(".").toAbsolutePath().normalize().toFile());

        try {
            Process process = processBuilder.start();

            // 결과 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String durationStr = reader.readLine();
                if (durationStr != null) {
                    process.destroy();
                    return (long) (Double.parseDouble(durationStr.trim()) * 1000);  // seconds to milliseconds
                }
            }

            // 에러 스트림 처리
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.warn(line);
                }
            }

            // FFprobe 실행 결과 확인
            int exitValue = process.waitFor();
            if (exitValue != 0) {
                throw new RuntimeException("FFprobe process failed with exit code: " + exitValue);
            }

            process.destroy();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to get TS duration", e);
        }

        return 0;
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
