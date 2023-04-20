package nk.service.NStory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LikesHistory {
    private int like_type;
    private int id;
    private String email;
}
