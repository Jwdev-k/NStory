package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.CommentDTO;
import nk.service.NStory.repository.CommentMapper;
import nk.service.NStory.service.CommentServiceIF;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentServiceIF {
    private final CommentMapper commentMapper;

    @Override
    public ArrayList<CommentDTO> getCommentList(int id) throws Exception {
        return commentMapper.getCommentList(id);
    }

    @Override
    public CommentDTO getComment(int cid) throws Exception {
        return commentMapper.getComment(cid);
    }

    @Transactional
    @Override
    public void addComment(CommentDTO commentDTO) throws Exception {
        commentMapper.addComment(commentDTO);
    }

    @Transactional
    @Override
    public void commentEdit(CommentDTO commentDTO) throws Exception {
        commentMapper.commentEdit(commentDTO);
    }

    @Transactional
    @Override
    public void deleteComment(int cid) throws Exception {
        commentMapper.deleteComment(cid);
    }

    @Override
    public int totalCount(int id) throws Exception {
        return commentMapper.totalCount(id);
    }
}
