package nk.service.NStory.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component @Slf4j
public class PageUtil {
    private int totalCount; // 총 게시글 수
    private int startPage;
    private int endPage;
    private boolean isShortPrev;
    private boolean isPrev;
    private boolean isNext;
    private boolean isShortNext;
    private int displayPageNum = 10; // 한페이지당 페이징번호 갯수
    private int page = 1; // 현재페이지
    private int perPageNum = 50; // 한 페이지 총 게시글 수
    private int lastEndPage;

    public void setTotalCount(int totalCount) { // 총 게시글 수 설정
        this.totalCount = totalCount;
        calcData();
    }

    private void calcData() {
        endPage = (int) (Math.ceil(getPage() / (double) displayPageNum) * displayPageNum);
        startPage = (endPage - displayPageNum) + 1;
        if (startPage <= 0) {
            startPage = 1;
        }
        lastEndPage = (int) (Math.ceil((double) getTotalCount() / getPerPageNum()));
        if (endPage >= lastEndPage) {
            endPage = lastEndPage;
        }
        isNext = (endPage + 1) * getPerPageNum() < totalCount;
        isShortNext = getEndPage() > getPage();
        isPrev = startPage != 1;
        isShortPrev = 1 < getPage();
    }
}

