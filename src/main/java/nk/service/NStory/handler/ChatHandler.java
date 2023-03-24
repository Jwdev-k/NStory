package nk.service.NStory.handler;

import nk.service.NStory.utils.CurrentTime;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class ChatHandler extends TextWebSocketHandler {

    private static final List<WebSocketSession> sessions = new ArrayList<>();
    private final StringBuffer sb = new StringBuffer();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
       TextMessage tm;
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                sb.append(CurrentTime.getTime2() + " 익명 : " + message.getPayload());
                if (message.getPayloadLength() > 0) {
                    tm = new TextMessage(sb.toString());
                    s.sendMessage(tm);
                    sb.delete(0, sb.length());
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
