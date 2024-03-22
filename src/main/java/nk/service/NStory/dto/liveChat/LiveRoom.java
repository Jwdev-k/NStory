package nk.service.NStory.dto.liveChat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.LinkedList;

@NoArgsConstructor
@Getter @Setter
public class LiveRoom {
    private String roomId; //UUID
    private String roomName;
    private String roomPassword;
    private String hostName; // this is UserName
    private int aid;
    private String email;
    private boolean isSecret;
    private static final LinkedList<WebSocketSession> sessions = new LinkedList<>(); // roomId, session

    public void addUser(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeUser(WebSocketSession session) {
        sessions.remove(session);
    }

    public LinkedList<WebSocketSession> getSessionList() {
        return sessions;
    }
}
