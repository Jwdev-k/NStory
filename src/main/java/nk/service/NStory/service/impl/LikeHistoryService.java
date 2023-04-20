package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.LikesHistory;
import nk.service.NStory.repository.LikesMapper;
import nk.service.NStory.service.LikeHistoryIF;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeHistoryService implements LikeHistoryIF {
    private final LikesMapper likesMapper;

    @Override
    public LikesHistory getLikeType(int id, String email) throws Exception {
        return likesMapper.getLikeType(id,email);
    }

    @Transactional
    @Override
    public void insertHistory(LikesHistory likesHistory) throws Exception {
        likesMapper.insertHistory(likesHistory);
    }

    @Transactional
    @Override
    public void updateLikeHistory(LikesHistory likesHistory) throws Exception {
        likesMapper.updateLikeHistory(likesHistory);
    }

    @Transactional
    @Override
    public void historyLikeCancel(LikesHistory likesHistory) throws Exception {
        likesMapper.deleteLikeCancel(likesHistory);
    }

    @Transactional
    @Override
    public void updateDisLikeHistory(LikesHistory likesHistory) throws Exception {
        likesMapper.updateDisLikeHistory(likesHistory);
    }

    @Transactional
    @Override
    public void historyDisLikeCancel(LikesHistory likesHistory) throws Exception {
        likesMapper.deleteDisLikeCancel(likesHistory);
    }
}
