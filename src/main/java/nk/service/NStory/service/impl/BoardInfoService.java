package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.ByteImageDTO;
import nk.service.NStory.dto.bbs.BoardInfo;
import nk.service.NStory.repository.BoardInfoMapper;
import nk.service.NStory.service.BoardInfoIF;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service @Slf4j
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

    @Transactional
    @Override
    public void updateSettings(byte[] mainImg, String subname, String bid) throws Exception {
        boardInfoMapper.updateSettings(mainImg, subname, bid);
        log.info("요청주소 : /whiteboard/setup " + "Action : 게시판설정 변경" + " bid : " + bid);
    }

    @Override
    public ByteImageDTO getMainImage(String bid) throws Exception {
        return boardInfoMapper.getMainImage(bid);
    }
}
