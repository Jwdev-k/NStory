package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.Enum.SearchType;
import nk.service.NStory.dto.WhiteBoard;
import nk.service.NStory.repository.WhiteBoardMapper;
import nk.service.NStory.service.WhiteBoardServiceIF;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class WhiteBoardService implements WhiteBoardServiceIF {
    private final WhiteBoardMapper whiteBoardMapper;

    @Override
    public ArrayList<WhiteBoard> boardList(String bid, int start) throws Exception {
        if (start == 1 || start < 1) {
            return whiteBoardMapper.boardList(bid, 0);
        } else {
            return whiteBoardMapper.boardList(bid, (start - 1) * 10);
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
    }

    @Transactional
    @Override
    public void deleteBoard(int id, String email) throws Exception {
        whiteBoardMapper.deleteBoard(id, email);
    }

    @Transactional
    @Override
    public void updateBoard(WhiteBoard wb) throws Exception {
        whiteBoardMapper.updateBoard(wb);
    }

    @Override
    public ArrayList<WhiteBoard> searchList(String bid, int start, SearchType type, String str) throws Exception {
        if (start == 1 || start < 1) {
            return whiteBoardMapper.searchList(bid, 0, type, str);
        } else {
            return whiteBoardMapper.searchList(bid, (start - 1) * 10, type, str);
        }
    }

    @Override
    public int searchTotalCount(String bid, SearchType type, String str) throws Exception {
        return whiteBoardMapper.searchTotalCount(bid, type, str);
    }
}
