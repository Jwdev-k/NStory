package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
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
    public ArrayList<WhiteBoard> boardList(int start) throws Exception {
        if (start == 1 || start < 1) {
            return whiteBoardMapper.boardList(0);
        } else {
            return whiteBoardMapper.boardList((start - 1) * 10);
        }
    }

    @Override
    public WhiteBoard getBoardView(int id) throws Exception {
        return whiteBoardMapper.getBoardView(id);
    }

    @Override
    public int totalCount() throws Exception {
        return whiteBoardMapper.totalCount();
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
}
