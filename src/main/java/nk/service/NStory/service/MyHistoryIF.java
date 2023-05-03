package nk.service.NStory.service;

import nk.service.NStory.dto.myhistory.HistoryCommentsDTO;
import nk.service.NStory.dto.myhistory.HistoryPostsDTO;

import java.util.ArrayList;

public interface MyHistoryIF {
    ArrayList<HistoryPostsDTO> getMyPosts(String email) throws Exception;
    ArrayList<HistoryCommentsDTO> getMyComments(String email) throws Exception;
}
