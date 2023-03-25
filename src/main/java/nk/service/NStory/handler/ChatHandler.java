package nk.service.NStory.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
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

public class ChatHandler extends TextWebSocketHandler {

    private static final List<WebSocketSession> sessions = new ArrayList<>();
    private static final List<String> userList = new ArrayList<>();
    private final StringBuffer sb = new StringBuffer();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        if (session.getPrincipal() != null) {
            userList.add(session.getPrincipal().getName());
        }
        sendUserList();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                if (message.getPayloadLength() > 0) {
                    ChatMessage chatMessage = new ChatMessage();
                    if (session.getPrincipal() != null) {
                        sb.append(CurrentTime.getTime2() + " " + session.getPrincipal().getName() +  " : " + message.getPayload());
                    } else {
                        sb.append(CurrentTime.getTime2() + " 익명 : " + message.getPayload());
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
        if (session.getPrincipal() != null) {
            userList.remove(session.getPrincipal().getName());
        }
       sendUserList();
    }

    public void sendUserList() throws IOException {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatType(ChatType.USER_LIST);
        chatMessage.setContent(objectMapper.writeValueAsString(userList.toString()));
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
        }
    }
}
