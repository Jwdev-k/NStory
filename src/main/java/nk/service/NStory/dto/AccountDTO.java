package nk.service.NStory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    private String creationDate; //19byte
    private boolean isEnable;
}
