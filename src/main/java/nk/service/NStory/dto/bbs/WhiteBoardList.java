package nk.service.NStory.dto.bbs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WhiteBoardList {
    private int id;
    private String title;
    private String author;
    private String creationDate;
    private int views;
    private int like_count;
    private int cm_rp_counts; // author table data
    private String kname;

    public WhiteBoardList(int id, String title, String author, String creationDate, int views, int like_count, int cm_rp_counts) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.creationDate = creationDate;
        this.views = views;
        this.like_count = like_count;
        this.cm_rp_counts = cm_rp_counts;
    }
}
