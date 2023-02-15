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
    private int level; //레벨
    private int exp; //경험치
    private int nCoin; // 코인 갯수 max 100000000
    private boolean isEnable;
}
