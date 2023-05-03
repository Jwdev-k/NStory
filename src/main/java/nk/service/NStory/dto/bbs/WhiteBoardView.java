package nk.service.NStory.dto.bbs;

import lombok.Getter;

@Getter
public class WhiteBoardView extends WhiteBoard {
    private final int aid;

    public WhiteBoardView(int id, String bid, String title, String contents, String author, String email, String creationDate, int views, int like_count, int dislike_count, boolean isNotice, boolean isEnable, int aid) {
        super(id, bid, title, contents, author, email, creationDate, views, like_count, dislike_count, isNotice, isEnable);
        this.aid = aid;
    }
}
