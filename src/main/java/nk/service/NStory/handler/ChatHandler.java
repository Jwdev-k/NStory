package nk.service.NStory.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.Enum.ChatType;
import nk.service.NStory.dto.liveChat.ChatMessage;
import nk.service.NStory.service.liveChat.ChatRoomService;
import nk.service.NStory.utils.CurrentTime;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChatHandler extends TextWebSocketHandler {

    private final ChatRoomService chatRoomService = new ChatRoomService();
    private static final Map<String, String> userList = new ConcurrentHashMap<>(); // roomId, session?..
    private final ObjectMapper objectMapper = new ObjectMapper(); //json

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 세션의 uri 에서 roomId 가져오기
        String[] path = session.getUri().toString().split("/");
        String roomId = path[path.length - 1];

        if (roomId != null) {
            if (session.getPrincipal() != null) {
                chatRoomService.joinRoom(roomId, session);
                userList.put(roomId, session.getPrincipal().getName());
            }
            sendUserList(roomId);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setChatType(ChatType.CHAT_TYPE);
            chatMessage.setContent(CurrentTime.getTime2() + " 시스템 : " + " 채팅방과 연결되었습니다.");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
        } else {
            log.error("roomId is null!");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 세션의 uri 에서 roomId 가져오기
        String[] path = session.getUri().toString().split("/");
        String roomId = path[path.length - 1];

        for (WebSocketSession s : chatRoomService.getRoom(roomId).getSessionList()) {
            if (s.isOpen()) {
                if (message.getPayloadLength() > 0) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setChatType(ChatType.CHAT_TYPE);
                    chatMessage.setContent(message.getPayload());
                    if (session.getPrincipal() != null) {
                        chatMessage.setUserName(session.getPrincipal().getName());
                        log.debug(chatMessage.toString());
                    } else {
                        chatMessage.setUserName("익명");
                        log.debug(chatMessage.toString());
                    }
                    chatMessage.setSendTime(CurrentTime.getTime2());
                    s.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 세션의 uri 에서 roomId 가져오기
        String[] path = session.getUri().toString().split("/");
        String roomId = path[path.length - 1];

        chatRoomService.leaveRoom(roomId, session);
        userList.remove(roomId);
        session.close();
        if (session.getPrincipal() != null) {
            userList.remove(session.getId());
        }
        sendUserList(roomId);
    }

    public void sendUserList(String roomId) throws IOException {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatType(ChatType.USER_LIST);
        chatMessage.setContent(objectMapper.writeValueAsString(userList.values()));
        for (WebSocketSession s : chatRoomService.getRoom(roomId).getSessionList()) {
            s.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
        }
    }
}
