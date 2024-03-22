package nk.service.NStory.service.liveChat;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.liveChat.LiveRoom;
import nk.service.NStory.utils.CurrentTime;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.LinkedHashMap;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {
    private static final LinkedHashMap<String, LiveRoom> Rooms = new LinkedHashMap<>();

    public void addRoom(LiveRoom liveRoom) {
        Rooms.put(liveRoom.getRoomId(), liveRoom);
        log.info(CurrentTime.getTime() + "/ RoomId: " + liveRoom.getRoomId() + " 채팅방이 추가되었습니다.");
    }

    public void removeRoom(String roomId) {
        Rooms.remove(roomId);
    }

    public LinkedHashMap<String, LiveRoom> getRooms() {
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setRoomName("테스트");
        liveRoom.setAid(17);
        liveRoom.setRoomName("테스트 룸 입니다.");
        liveRoom.setSecret(false);
        liveRoom.setHostName("GomGom");
        liveRoom.setRoomId(UUID.randomUUID().toString());
        Rooms.put(liveRoom.getRoomId(), liveRoom);
        return Rooms;
    }

    public LiveRoom getRoom(String roomId) {
        return Rooms.get(roomId);
    }

    public void joinRoom(String roomId, WebSocketSession session) {
        if (Rooms.get(roomId) != null) {
            Rooms.get(roomId).addUser(session);
        } else {
            log.error(roomId + "존재하지 않는 방 입니다.");
        }
    }

    public void leaveRoom(String roomId, WebSocketSession session) {
        if (Rooms.get(roomId) != null) {
            Rooms.get(roomId).removeUser(session);
        } else {
            log.error(roomId + "존재하지 않는 방 입니다.");
        }
    }
}
