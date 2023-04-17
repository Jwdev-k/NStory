package nk.service.NStory.service;

import nk.service.NStory.dto.CommentDTO;

import java.util.ArrayList;

public interface CommentServiceIF {
    ArrayList<CommentDTO> getCommentList(int id) throws Exception;
    CommentDTO getComment(int cid) throws Exception;
    void addComment(CommentDTO commentDTO) throws Exception;
    void commentEdit(CommentDTO commentDTO) throws Exception;
    void deleteComment(int cid) throws Exception;
}
