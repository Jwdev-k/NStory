package nk.service.NStory.dto.bbs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardInfo {
    private String bid;
    private String kname;
    private String subname;
    private String email;
    private byte[] mainImg;
    private String username;

    public BoardInfo(String bid, String kname) { //TODO getBoardNameList() 메서드에서 사용중
        this.bid = bid;
        this.kname = kname;
    }
}
