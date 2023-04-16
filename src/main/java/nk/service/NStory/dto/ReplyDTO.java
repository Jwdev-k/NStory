package nk.service.NStory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReplyDTO {
    private int rid;
    private int cid;
    private int id;
    private String email;
    private String name;
    private String contents;
    private String time;
    private boolean isEnable;

    public ReplyDTO(int rid, String name, String contents) {
        this.rid = rid;
        this.name = name;
        this.contents = contents;
    }
}
