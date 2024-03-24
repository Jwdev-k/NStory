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
    private final LinkedList<WebSocketSession> chatSessions = new LinkedList<>(); // roomId, session
    private final LinkedList<WebSocketSession> liveSessions = new LinkedList<>(); // roomId, session

    public void addUser(WebSocketSession session) {
        chatSessions.add(session);
    }

    public void removeUser(WebSocketSession session) {
        chatSessions.remove(session);
    }

    public LinkedList<WebSocketSession> getSessionList() {
        return chatSessions;
    }


    public void addLiveUser(WebSocketSession session) {
        liveSessions.add(session);
    }

    public void removeLiveUser(WebSocketSession session) {
        liveSessions.remove(session);
    }

    public LinkedList<WebSocketSession> getLiveSessionList() {
        return liveSessions;
    }
}
