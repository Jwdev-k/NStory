package nk.service.NStory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WhiteBoard {
    private int id;
    private String title;
    private String contents;
    private String author;
    private String email;
    private String creationDate;
    private boolean isEnable;
}
