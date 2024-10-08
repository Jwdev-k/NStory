package nk.service.NStory.dto.record;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecordLogDTO {
    private int id;
    private String contents;
    private String email;
    private String name;
    private String time; //19byte
}
