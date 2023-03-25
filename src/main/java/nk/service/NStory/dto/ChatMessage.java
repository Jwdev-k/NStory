package nk.service.NStory.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nk.service.NStory.dto.Enum.ChatType;

@NoArgsConstructor
@Getter @Setter
public class ChatMessage {
    private ChatType chatType;
    private String content;
}
