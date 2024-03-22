package nk.service.NStory.dto.liveChat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nk.service.NStory.dto.Enum.ChatType;

@NoArgsConstructor
@Getter @Setter
@ToString
public class ChatMessage {
    private ChatType chatType;
    private String userName;
    private String content;
    private String sendTime;
}
