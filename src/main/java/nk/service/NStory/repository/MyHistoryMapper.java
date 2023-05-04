package nk.service.NStory.repository;

import nk.service.NStory.dto.myhistory.HistoryCommentsDTO;
import nk.service.NStory.dto.myhistory.HistoryPostsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface MyHistoryMapper {
    @Select("SELECT id, title, creationDate FROM whiteboard WHERE email = #{email} AND isEnable = 1 ORDER BY id DESC")
    ArrayList<HistoryPostsDTO> getMyPosts(String email) throws Exception;

    @Select("SELECT id, contents, time FROM comment WHERE email = #{email} AND isEnable = 1 UNION SELECT id, contents, time FROM reply WHERE email = #{email} AND isEnable = 1 ORDER BY time DESC")
    ArrayList<HistoryCommentsDTO> getMyComments(String email) throws Exception;
}
