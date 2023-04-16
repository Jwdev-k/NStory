package nk.service.NStory.service;

import nk.service.NStory.dto.Enum.SearchType;
import nk.service.NStory.dto.WhiteBoard;

import java.util.ArrayList;

public interface WhiteBoardServiceIF {
    ArrayList<WhiteBoard> boardList(String bid,int start) throws Exception;
    WhiteBoard getBoardView(int id) throws Exception;
    int totalCount(String bid) throws Exception;
    void insertBoard(WhiteBoard wb) throws Exception;
    void deleteBoard(int id, String email) throws Exception;
    void updateBoard(WhiteBoard wb) throws Exception;
    ArrayList<WhiteBoard> searchList(String bid, int start, SearchType type, String str) throws Exception;
    int searchTotalCount(String bid, SearchType type, String str) throws Exception;
}
