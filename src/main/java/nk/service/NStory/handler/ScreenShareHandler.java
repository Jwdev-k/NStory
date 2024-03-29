package nk.service.NStory.handler;

import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.service.liveChat.ChatRoomService;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
            Path videoPath = Paths.get(System.getProperty("user.dir") + File.separator + "liveVideos"
                    + File.separator + roomId + File.separator + roomId + ".webm");
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

        String baseDir = System.getProperty("user.dir") + File.separator + "liveVideos";
        String roomDir = baseDir + File.separator + roomId;

        // roomId 이름의 폴더가 존재하지 않으면 폴더 생성
        File directory = new File(roomDir);
        if (!directory.exists()) {
            directory.mkdirs(); // 중첩된 디렉터리를 생성
        }

        // liveVideos 폴더에 roomId.webm 파일로 영상 데이터 저장 또는 추가
        File videoFile = new File(roomDir + File.separator + roomId + ".webm");
        try (FileOutputStream fileOutputStream = new FileOutputStream(videoFile, true)) { // true: append 모드
            fileOutputStream.write(mediaData); // 영상 데이터 추가

            for (WebSocketSession s : sessionList) {
                if (s.isOpen()) {
                    CompletableFuture.runAsync(() -> {
                        try {
                            //Path videoPath = Paths.get(System.getProperty("user.dir") + File.separator + "liveVideos" + File.separator + roomId + ".webm");
                            s.sendMessage(new BinaryMessage(mediaData));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            log.info("영상 데이터 보냄 길이: " + message.getPayloadLength());
        }
    }
}
