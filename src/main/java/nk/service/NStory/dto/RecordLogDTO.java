package nk.service.NStory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class RecordLogDTO {
    private int id;
    private String contents;
    private String name;
    private LocalDateTime time;
}
