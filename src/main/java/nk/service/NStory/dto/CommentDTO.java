package nk.service.NStory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDTO {
    private int cid;
    private int id;
    private String email;
    private String name;
    private String contents;
    private String time;
    private boolean isEnable;

    public CommentDTO(int cid, String name, String contents) {
        this.cid = cid;
        this.name = name;
        this.contents = contents;
    }
}
