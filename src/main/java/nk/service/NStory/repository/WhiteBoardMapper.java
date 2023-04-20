package nk.service.NStory.repository;

import nk.service.NStory.dto.Enum.SearchType;
import nk.service.NStory.dto.WhiteBoard;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface WhiteBoardMapper {
    @Select("SELECT id, title, author, creationDate, views FROM whiteboard WHERE isEnable = 1 AND bid = #{bid} ORDER BY id DESC LIMIT #{start}, 50")
    ArrayList<WhiteBoard> boardList(@Param("bid") String bid, @Param("start") int start) throws Exception;
    @Select("SELECT * FROM whiteboard WHERE isEnable = 1 AND id = #{id}")
    WhiteBoard getBoardView(int id) throws Exception;
    @Select("SELECT count(*) FROM whiteboard WHERE isEnable = 1 AND bid = #{bid}")
    int totalCount(String bid) throws Exception;
    @Insert("INSERT INTO whiteboard VALUE(null, #{bid}, #{title}, #{contents}, #{author}, #{email}, #{creationDate}, #{views}, #{like}, #{dislike}, #{isEnable})")
    void insertBoard(WhiteBoard wb) throws Exception;
    @Delete("DELETE FROM whiteboard WHERE id = #{id} AND email = #{email}")
    void deleteBoard(@Param("id") int id, @Param("email")String email) throws Exception;
    @Update("UPDATE whiteboard SET title = #{title}, contents = #{contents}, author = #{author} WHERE id = #{id} AND email = #{email}")
    void updateBoard(WhiteBoard wb) throws Exception;
    @Select("SELECT id, title, author, creationDate, views FROM whiteboard WHERE isEnable = 1 AND ${type} LIKE CONCAT('%', #{str}, '%') AND bid = #{bid} ORDER BY id DESC LIMIT #{start}, 50")
    ArrayList<WhiteBoard> searchList(@Param("bid") String bid, @Param("start")int start, @Param("type")SearchType type, @Param("str")String str) throws Exception;
    @Select("SELECT count(*) FROM whiteboard WHERE isEnable = 1 AND ${type} LIKE CONCAT('%', #{str},'%') AND bid = #{bid}")
    int searchTotalCount(@Param("bid") String bid, @Param("type")SearchType type, @Param("str")String str) throws Exception;
    @Update("UPDATE whiteboard SET views = views + 1 WHERE id = #{id}")
    void updateViews(int id) throws Exception;
    @Update("UPDATE whiteboard SET like_count = like_count + 1 WHERE id = #{id}")
    void updateLike(int id) throws Exception;
    @Update("UPDATE whiteboard SET like_count = like_count - 1 WHERE id = #{id}")
    void updateLikeCancel(int id) throws Exception;
    @Update("UPDATE whiteboard SET dislike_count = dislike_count + 1 WHERE id = #{id}")
    void updateDLike(int id) throws Exception;
    @Update("UPDATE whiteboard SET dislike_count = dislike_count - 1 WHERE id = #{id}")
    void updateDisLikeCancel(int id) throws Exception;
}
