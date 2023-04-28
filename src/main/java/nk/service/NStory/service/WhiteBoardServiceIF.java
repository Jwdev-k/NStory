package nk.service.NStory.service;

import nk.service.NStory.dto.Enum.SearchType;
import nk.service.NStory.dto.bbs.WhiteBoard;
import nk.service.NStory.dto.bbs.WhiteBoardList;

import java.util.ArrayList;

public interface WhiteBoardServiceIF {
    ArrayList<WhiteBoardList> boardList(String bid, int start) throws Exception;
    WhiteBoard getBoardView(int id) throws Exception;
    int totalCount(String bid) throws Exception;
    void insertBoard(WhiteBoard wb) throws Exception;
    void deleteBoard(int id, String email) throws Exception;
    void updateBoard(WhiteBoard wb) throws Exception;
    ArrayList<WhiteBoardList> searchList(String bid, int start, SearchType type, String str) throws Exception;
    int searchTotalCount(String bid, SearchType type, String str) throws Exception;
    void updateViews(int id) throws Exception;
    void updateLike(int id) throws Exception;
    void updateLikeCancel(int id) throws Exception;
    void updateDLike(int id) throws Exception;
    void updateDisLikeCancel(int id) throws Exception;
    ArrayList<WhiteBoardList> getNoticeList(String bid) throws Exception;
    ArrayList<WhiteBoardList> getBestList() throws Exception;
    ArrayList<WhiteBoardList> getRecentList() throws Exception;
    ArrayList<WhiteBoardList> getMainNoticeList() throws Exception;
}
