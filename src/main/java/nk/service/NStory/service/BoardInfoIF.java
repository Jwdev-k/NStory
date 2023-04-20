package nk.service.NStory.service;

import nk.service.NStory.dto.BoardInfo;

import java.util.ArrayList;

public interface BoardInfoIF {
    BoardInfo getBoardInfo(String bid) throws Exception;
    ArrayList<BoardInfo> getBoardNameList() throws Exception;
}
