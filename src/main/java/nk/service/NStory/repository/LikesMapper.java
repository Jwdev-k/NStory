package nk.service.NStory.repository;

import nk.service.NStory.dto.LikesHistory;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LikesMapper {
    @Select("SELECT * FROM likes_history WHERE email = #{email} AND id = #{id}")
    LikesHistory getLikeType(@Param("id") int id, @Param("email") String email) throws Exception;
    @Insert("INSERT INTO likes_history VALUE(#{like_type}, #{id}, #{email})")
    void insertHistory(LikesHistory likesHistory) throws Exception;
    @Update("UPDATE likes_history SET like_type = #{like_type} WHERE email = #{email} AND id = #{id}")
    void updateLikeHistory(LikesHistory likesHistory) throws Exception;
    @Delete("DELETE FROM likes_history WHERE email = #{email} AND id = #{id}")
    void deleteLikeCancel(LikesHistory likesHistory) throws Exception;
    @Update("UPDATE likes_history SET like_type = #{like_type} WHERE email = #{email} AND id = #{id}")
    void updateDisLikeHistory(LikesHistory likesHistory) throws Exception;
    @Delete("DELETE FROM likes_history WHERE email = #{email} AND id = #{id}")
    void deleteDisLikeCancel(LikesHistory likesHistory) throws Exception;
}
