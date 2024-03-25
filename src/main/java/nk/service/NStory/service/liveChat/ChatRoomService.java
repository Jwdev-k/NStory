package nk.service.NStory.service.liveChat;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.liveChat.LiveRoom;
import nk.service.NStory.utils.CurrentTime;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.LinkedList;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {
    private static final LinkedList<LiveRoom> Rooms = new LinkedList<>();

    public void addRoom(LiveRoom liveRoom) {
        Rooms.add(liveRoom);
        log.info(CurrentTime.getTime() + "/ RoomId: " + liveRoom.getRoomId() + " 채팅방이 추가되었습니다.");
    }

    public void removeRoom(String roomId) {
        Rooms.removeIf(lr -> lr.getRoomId().equals(roomId));
    }

    public LinkedList<LiveRoom> getRooms() {
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setRoomName("테스트");
        liveRoom.setAid(2);
        liveRoom.setRoomName("테스트 룸 입니다.");
        liveRoom.setSecret(false);
        liveRoom.setHostName("GomGom");
        liveRoom.setRoomId(UUID.randomUUID().toString());
        Rooms.add(liveRoom);
        return Rooms;
    }

    public LiveRoom getRoom(String roomId) {
        for(LiveRoom lr : Rooms) {
            if (lr.getRoomId().equals(roomId)) {
                return lr;
            }
        }
        return null;
    }

    public void joinRoom(String roomId, WebSocketSession session) {
        for (LiveRoom room : Rooms) {
            if (room.getRoomId().equals(roomId)) {
                room.addUser(session);
                log.info("roomId: " + roomId + ", sessionId: " + session.getId() + " JOIN! " + CurrentTime.getTime());
                return;
            }
        }
        log.error(roomId + "존재하지 않는 방 입니다.");
    }

    public void leaveRoom(String roomId, WebSocketSession session) {
        for (LiveRoom room : Rooms) {
            if (room.getRoomId().equals(roomId)) {
                room.removeUser(session);
                log.info("roomId: " + roomId + ", sessionId: " + session.getId() + " Leave! " + CurrentTime.getTime());
                return;
            }
        }
        log.error(roomId + "존재하지 않는 방 입니다.");
    }

    public void joinLiveShare(String roomId, WebSocketSession session) {
        for (LiveRoom room : Rooms) {
            if (room.getRoomId().equals(roomId)) {
                room.addLiveUser(session);
                log.info("roomId: " + roomId + ", sessionId: " + session.getId() + " JOIN! " + CurrentTime.getTime());
                return;
            }
        }
        log.error(roomId + "존재하지 않는 방 입니다.");
    }

    public void leaveLiveShare(String roomId, WebSocketSession session) {
        for (LiveRoom room : Rooms) {
            if (room.getRoomId().equals(roomId)) {
                room.removeLiveUser(session);
                log.info("roomId: " + roomId + ", sessionId: " + session.getId() + " Leave! " + CurrentTime.getTime());
                return;
            }
        }
        log.error(roomId + "존재하지 않는 방 입니다.");
    }
}
