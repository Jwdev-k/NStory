package nk.service.NStory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AccountDTO {
    private int id;
    private String email;
    private String password;
    private String name;
    private String comment;
    private byte[] profileImg;
    private String role;
    private LocalDateTime creationDate;
    private boolean isEnable;
}
