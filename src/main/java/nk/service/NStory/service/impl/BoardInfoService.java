package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.BoardInfo;
import nk.service.NStory.repository.BoardInfoMapper;
import nk.service.NStory.service.BoardInfoIF;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BoardInfoService implements BoardInfoIF {
    private final BoardInfoMapper boardInfoMapper;
    @Override
    public BoardInfo getBoardInfo(String bid) throws Exception {
        return boardInfoMapper.getBoardInfo(bid);
    }

    @Override
    public ArrayList<BoardInfo> getBoardNameList() throws Exception {
        return boardInfoMapper.getBoardNameList();
    }
}
