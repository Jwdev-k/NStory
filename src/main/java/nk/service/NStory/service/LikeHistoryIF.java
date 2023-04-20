package nk.service.NStory.service;

import nk.service.NStory.dto.LikesHistory;

public interface LikeHistoryIF {
    LikesHistory getLikeType(int id, String email) throws Exception;
    void insertHistory(LikesHistory likesHistory) throws Exception;
    void updateLikeHistory(LikesHistory likesHistory) throws Exception;
    void historyLikeCancel(LikesHistory likesHistory) throws Exception;
    void updateDisLikeHistory(LikesHistory likesHistory) throws Exception;
    void historyDisLikeCancel(LikesHistory likesHistory) throws Exception;
}
