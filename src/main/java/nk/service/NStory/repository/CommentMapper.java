package nk.service.NStory.repository;

import nk.service.NStory.dto.CommentDTO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface CommentMapper {
    @Select("SELECT c.*, a.id as aid FROM comment c INNER JOIN account a ON a.email = c.email WHERE c.isEnable = 1 AND c.id = #{id} ORDER BY c.cid DESC")
    ArrayList<CommentDTO> getCommentList(int id) throws Exception;
    @Select("SELECT * FROM comment WHERE isEnable = 1 AND cid = #{cid}")
    CommentDTO getComment(int cid) throws Exception;
    @Insert("INSERT INTO comment VALUE(null, #{id}, #{email}, #{name}, #{contents}, #{time}, #{isEnable})")
    void addComment(CommentDTO commentDTO) throws Exception;
    @Update("UPDATE comment SET name= #{name}, contents = #{contents} WHERE cid = #{cid} AND email = #{email}")
    void commentEdit(CommentDTO commentDTO) throws Exception;
    @Delete("DELETE FROM comment WHERE cid = #{cid}")
    void deleteComment(@Param("cid") int cid) throws Exception;
}
