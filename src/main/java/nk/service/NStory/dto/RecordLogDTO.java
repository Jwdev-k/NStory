package nk.service.NStory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecordLogDTO {
    private int id;
    private String contents;
    private String name;
    private String time; //19byte
}
