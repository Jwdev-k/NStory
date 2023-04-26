package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.Enum.SearchType;
import nk.service.NStory.dto.bbs.WhiteBoard;
import nk.service.NStory.dto.bbs.WhiteBoardList;
import nk.service.NStory.repository.WhiteBoardMapper;
import nk.service.NStory.service.WhiteBoardServiceIF;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service @Slf4j
@RequiredArgsConstructor
public class WhiteBoardService implements WhiteBoardServiceIF {
    private final WhiteBoardMapper whiteBoardMapper;

    @Override
    public ArrayList<WhiteBoardList> boardList(String bid, int start) throws Exception {
        if (start == 1 || start < 1) {
            return whiteBoardMapper.boardList(bid, 0);
        } else {
            return whiteBoardMapper.boardList(bid, (start - 1) * 50);
        }
    }

    @Override
    public WhiteBoard getBoardView(int id) throws Exception {
        return whiteBoardMapper.getBoardView(id);
    }

    @Override
    public int totalCount(String bid) throws Exception {
        return whiteBoardMapper.totalCount(bid);
    }

    @Transactional
    @Override
    public void insertBoard(WhiteBoard wb) throws Exception {
        whiteBoardMapper.insertBoard(wb);
        log.info("요청주소 : /whiteboard/add\n" + "Action : whiteboard 작성" + "\n 요청자: " + wb.getEmail());
    }

    @Transactional
    @Override
    public void deleteBoard(int id, String email) throws Exception {
        whiteBoardMapper.deleteBoard(id, email);
        log.info("요청주소 : /whiteboard/delete" + "Action : whiteboard 삭제" + "\n 요청자: " + email);
    }

    @Transactional
    @Override
    public void updateBoard(WhiteBoard wb) throws Exception {
        whiteBoardMapper.updateBoard(wb);
        log.info("요청주소 : /whiteboard/update\n" + "Action : whiteboard 수정" + "\n 요청자: " + wb.getEmail());
    }

    @Override
    public ArrayList<WhiteBoardList> searchList(String bid, int start, SearchType type, String str) throws Exception {
        if (start == 1 || start < 1) {
            return whiteBoardMapper.searchList(bid, 0, type, str);
        } else {
            return whiteBoardMapper.searchList(bid, (start - 1) * 50, type, str);
        }
    }

    @Override
    public int searchTotalCount(String bid, SearchType type, String str) throws Exception {
        return whiteBoardMapper.searchTotalCount(bid, type, str);
    }

    @Transactional
    @Override
    public void updateViews(int id) throws Exception {
        whiteBoardMapper.updateViews(id);
    }

    @Transactional
    @Override
    public void updateLike(int id) throws Exception {
        whiteBoardMapper.updateLike(id);
    }

    @Transactional
    @Override
    public void updateLikeCancel(int id) throws Exception {
        whiteBoardMapper.updateLikeCancel(id);
    }

    @Transactional
    @Override
    public void updateDLike(int id) throws Exception {
        whiteBoardMapper.updateDLike(id);
    }

    @Transactional
    @Override
    public void updateDisLikeCancel(int id) throws Exception {
        whiteBoardMapper.updateDisLikeCancel(id);
    }

    @Override
    public ArrayList<WhiteBoardList> getNoticeList(String bid) throws Exception {
        return whiteBoardMapper.getNoticeList(bid);
    }

    @Override
    public ArrayList<WhiteBoardList> getBestList() throws Exception {
        return whiteBoardMapper.getBestList();
    }
}
