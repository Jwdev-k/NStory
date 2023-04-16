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
    private boolean isEnable;

    public WhiteBoard(int id, String title, String author, String creationDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.creationDate = creationDate;
    }

    public WhiteBoard(int id, String title, String contents, String author, String email) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.email = email;
    }
}
