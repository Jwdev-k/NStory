package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.myhistory.HistoryCommentsDTO;
import nk.service.NStory.dto.myhistory.HistoryPostsDTO;
import nk.service.NStory.repository.MyHistoryMapper;
import nk.service.NStory.service.MyHistoryIF;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MyHistoryService implements MyHistoryIF {
    private final MyHistoryMapper historyMapper;

    @Override
    public ArrayList<HistoryPostsDTO> getMyPosts(String email) throws Exception {
        return historyMapper.getMyPosts(email);
    }

    @Override
    public ArrayList<HistoryCommentsDTO> getMyComments(String email) throws Exception {
        return historyMapper.getMyComments(email);
    }
}
