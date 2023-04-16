package nk.service.NStory.service;

import nk.service.NStory.dto.ReplyDTO;

import java.util.ArrayList;

public interface ReplyServiceIF {
    ArrayList<ReplyDTO> getReplyList(int id) throws Exception;
    ReplyDTO getReply(int rid) throws Exception;
    void addReply(ReplyDTO replyDTO) throws Exception;
    void replyEdit(ReplyDTO replyDTO) throws Exception;
    void deleteReply(int rid) throws Exception;
    int totalCount(int id) throws Exception;
}
