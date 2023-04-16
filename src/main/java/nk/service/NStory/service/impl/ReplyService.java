package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.ReplyDTO;
import nk.service.NStory.repository.ReplyMapper;
import nk.service.NStory.service.ReplyServiceIF;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ReplyService implements ReplyServiceIF {
    private final ReplyMapper replyMapper;

    @Override
    public ArrayList<ReplyDTO> getReplyList(int id) throws Exception {
        return replyMapper.getReplyList(id);
    }

    @Override
    public ReplyDTO getReply(int rid) throws Exception {
        return replyMapper.getReply(rid);
    }

    @Transactional
    @Override
    public void addReply(ReplyDTO replyDTO) throws Exception {
        replyMapper.addReply(replyDTO);
    }

    @Transactional
    @Override
    public void replyEdit(ReplyDTO replyDTO) throws Exception {
        replyMapper.replyEdit(replyDTO);
    }

    @Transactional
    @Override
    public void deleteReply(int rid) throws Exception {
        replyMapper.deleteReply(rid);
    }

    @Override
    public int totalCount(int id) throws Exception {
        return replyMapper.totalCount(id);
    }
}
