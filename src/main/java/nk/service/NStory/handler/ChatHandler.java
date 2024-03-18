package nk.service.NStory.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.ChatMessage;
import nk.service.NStory.dto.Enum.ChatType;
import nk.service.NStory.utils.CurrentTime;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChatHandler extends TextWebSocketHandler {

    private static final List<WebSocketSession> sessions = new ArrayList<>();
    private static final Map<String, String> userList = new ConcurrentHashMap<>();
    private final StringBuffer sb = new StringBuffer();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        if (session.getPrincipal() != null) {
            userList.put(session.getId(), session.getPrincipal().getName());
        }
        sendUserList();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatType(ChatType.CHAT_TYPE);
        chatMessage.setContent(CurrentTime.getTime2() + " 시스템 : " + "채팅방과 연결되었습니다.");
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                if (message.getPayloadLength() > 0) {
                    ChatMessage chatMessage = new ChatMessage();
                    if (session.getPrincipal() != null) {
                        sb.append(CurrentTime.getTime2() + " " + session.getPrincipal().getName() +  " : " + message.getPayload());
                        log.debug(session.getPrincipal().getName() +  " : " + message.getPayload());
                    } else {
                        sb.append(CurrentTime.getTime2() + " 익명 : " + message.getPayload());
                        log.debug("익명 : " + message.getPayload());
                    }
                    chatMessage.setChatType(ChatType.CHAT_TYPE);
                    chatMessage.setContent(sb.toString());
                    s.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
                    sb.delete(0, sb.length());
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        session.close();
        if (session.getPrincipal() != null) {
            userList.remove(session.getId());
        }
       sendUserList();
    }

    public void sendUserList() throws IOException {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatType(ChatType.USER_LIST);
        chatMessage.setContent(objectMapper.writeValueAsString(userList.values()));
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
        }
    }
}
