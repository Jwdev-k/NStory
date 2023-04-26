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
}
