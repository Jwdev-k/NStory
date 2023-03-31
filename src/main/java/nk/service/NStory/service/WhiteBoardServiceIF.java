package nk.service.NStory.service;

import nk.service.NStory.dto.WhiteBoard;

import java.util.ArrayList;

public interface WhiteBoardServiceIF {
    ArrayList<WhiteBoard> boardList(int start) throws Exception;
    WhiteBoard getBoardView(int id) throws Exception;
    int totalCount() throws Exception;
    void insertBoard(WhiteBoard wb) throws Exception;
    void deleteBoard(int id, String email) throws Exception;
}
