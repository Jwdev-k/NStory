package nk.service.NStory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WhiteBoard {
    private int id;
    private String bid;
    private String title;
    private String contents;
    private String author;
    private String email;
    private String creationDate;
    private int views;
    private int like_count;
    private int dislike_count;
    private boolean isNotice;
    private boolean isEnable;

    public WhiteBoard(int id, String title, String author, String creationDate, int views, int like_count) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.creationDate = creationDate;
        this.views = views;
        this.like_count = like_count;
    }

    public WhiteBoard(int id, String title, String contents, String author, String email, boolean isNotice) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.email = email;
        this.isNotice = isNotice;
    }
}
