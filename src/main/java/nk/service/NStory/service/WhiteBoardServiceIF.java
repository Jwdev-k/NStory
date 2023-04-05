package nk.service.NStory.service;

import nk.service.NStory.dto.Enum.SearchType;
import nk.service.NStory.dto.WhiteBoard;

import java.util.ArrayList;

public interface WhiteBoardServiceIF {
    ArrayList<WhiteBoard> boardList(int start) throws Exception;
    WhiteBoard getBoardView(int id) throws Exception;
    int totalCount() throws Exception;
    void insertBoard(WhiteBoard wb) throws Exception;
    void deleteBoard(int id, String email) throws Exception;
    void updateBoard(WhiteBoard wb) throws Exception;
    ArrayList<WhiteBoard> searchList(int start, SearchType type, String str) throws Exception;
    int searchTotalCount(SearchType type, String str) throws Exception;
}
