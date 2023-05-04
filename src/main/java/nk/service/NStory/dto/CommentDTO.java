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
    private int aid;

    public CommentDTO(int cid, String email, String name, String contents) {
        this.cid = cid;
        this.email = email;
        this.name = name;
        this.contents = contents;
    }

    public CommentDTO(int cid, int id, String email, String name, String contents, String time, boolean isEnable) {
        this.cid = cid;
        this.id = id;
        this.email = email;
        this.name = name;
        this.contents = contents;
        this.time = time;
        this.isEnable = isEnable;
    }
}
