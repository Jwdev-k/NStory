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
    private int aid;

    public ReplyDTO(int rid, String name, String contents) {
        this.rid = rid;
        this.name = name;
        this.contents = contents;
    }

    public ReplyDTO(int rid, int cid, int id, String email, String name, String contents, String time, boolean isEnable) {
        this.rid = rid;
        this.cid = cid;
        this.id = id;
        this.email = email;
        this.name = name;
        this.contents = contents;
        this.time = time;
        this.isEnable = isEnable;
    }
}
