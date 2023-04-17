package nk.service.NStory.repository;

import nk.service.NStory.dto.ReplyDTO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface ReplyMapper {
    @Select("SELECT r.*, a.id as aid FROM reply r INNER JOIN account a ON a.email = r.email WHERE r.isEnable = 1 AND r.id = #{id}")
    ArrayList<ReplyDTO> getReplyList(int id) throws Exception;
    @Select("SELECT * FROM reply WHERE isEnable = 1 AND rid = #{rid}")
    ReplyDTO getReply(int rid) throws Exception;
    @Insert("INSERT INTO reply VALUE(null, #{cid}, #{id}, #{email}, #{name}, #{contents}, #{time}, #{isEnable})")
    void addReply(ReplyDTO replyDTO) throws Exception;
    @Update("UPDATE reply SET name = #{name},contents = #{contents} WHERE rid = #{rid}")
    void replyEdit(ReplyDTO replyDTO) throws Exception;
    @Delete("DELETE FROM reply WHERE rid = #{rid}")
    void deleteReply(int rid) throws Exception;
}
