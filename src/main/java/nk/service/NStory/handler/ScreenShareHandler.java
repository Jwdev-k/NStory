package nk.service.NStory.handler;

import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.service.liveChat.ChatRoomService;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ScreenShareHandler extends BinaryWebSocketHandler {
    private static final ChatRoomService chatRoomService = new ChatRoomService();
    private final ConcurrentHashMap<String, ByteArrayOutputStream> videoChunks = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 세션의 uri 에서 roomId 가져오기
        String[] path = session.getUri().toString().split("/");
        String roomId = path[path.length - 1];

        if (roomId != null) {
            chatRoomService.joinLiveShare(roomId, session);
            Path videoPath = Paths.get(System.getProperty("user.dir") + File.separator + "liveVideos" + File.separator + roomId + ".webm");
            if (Files.exists(videoPath)) {
                session.sendMessage(new BinaryMessage(Files.readAllBytes(videoPath)));
            }
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
        // 세션의 uri 에서 roomId 가져오기
        String[] path = session.getUri().toString().split("/");
        String roomId = path[path.length - 1];

        List<WebSocketSession> sessionList = chatRoomService.getRoom(roomId).getLiveSessionList();

        byte[] mediaData = message.getPayload().array();
        // liveVideos 폴더에 roomId.webm 파일로 영상 데이터 저장 또는 추가
        File videoFile = new File(System.getProperty("user.dir") + File.separator + "liveVideos" + File.separator + roomId + ".webm");
        try (FileOutputStream fileOutputStream = new FileOutputStream(videoFile, true)) { // true: append 모드
            fileOutputStream.write(mediaData); // 영상 데이터 추가

            for (WebSocketSession s : sessionList) {
                if (s.isOpen()) {
                    CompletableFuture.runAsync(() -> {
                        try {
                            //Path videoPath = Paths.get(System.getProperty("user.dir") + File.separator + "liveVideos" + File.separator + roomId + ".webm");
                            s.sendMessage(new BinaryMessage(mediaData));
                            log.info("영상 데이터 보냄 길이: " + message.getPayloadLength());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }

    private byte[] createDummyWebM() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // WebM 파일 헤더 및 더미 데이터 생성
        // 실제 WebM 파일 헤더와 일치하지 않을 수 있으므로 주의가 필요합니다.
        dos.writeBytes("webm"); // WebM 파일 헤더
        dos.writeInt(1024 - 4); // WebM 더미 데이터 크기 (1KB - 4바이트 헤더 크기)

        // 1KB 크기의 더미 데이터 생성
        byte[] dummyBytes = new byte[1024 - 4]; // 1KB - 4바이트 헤더 크기
        dos.write(dummyBytes);

        dos.close();
        return baos.toByteArray();
    }
}
