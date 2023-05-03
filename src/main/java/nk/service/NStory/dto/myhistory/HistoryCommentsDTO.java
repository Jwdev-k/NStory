package nk.service.NStory.dto.myhistory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HistoryCommentsDTO {
    private int id;
    private String contents;
    private String time;
}
