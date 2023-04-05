package nk.service.NStory.repository;

import nk.service.NStory.dto.Enum.SearchType;
import nk.service.NStory.dto.WhiteBoard;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface WhiteBoardMapper {
    @Select("SELECT id, title, author, creationDate FROM whiteboard WHERE isEnable = 1 ORDER BY id DESC LIMIT #{start}, 10")
    ArrayList<WhiteBoard> boardList(int start) throws Exception;
    @Select("SELECT * FROM whiteboard WHERE isEnable = 1 AND id = #{id}")
    WhiteBoard getBoardView(int id) throws Exception;
    @Select("SELECT count(*) FROM whiteboard WHERE isEnable = 1")
    int totalCount() throws Exception;
    @Insert("INSERT INTO whiteboard VALUE(null, #{title}, #{contents}, #{author}, #{email}, #{creationDate}, #{isEnable})")
    void insertBoard(WhiteBoard wb) throws Exception;
    @Delete("DELETE FROM whiteboard WHERE id = #{id} AND email = #{email}")
    void deleteBoard(@Param("id") int id, @Param("email")String email) throws Exception;
    @Update("UPDATE whiteboard SET title = #{title}, contents = #{contents}, author = #{author} WHERE id = #{id} AND email = #{email}")
    void updateBoard(WhiteBoard wb) throws Exception;
    @Select("SELECT id, title, author, creationDate FROM whiteboard WHERE isEnable = 1 AND ${type} LIKE CONCAT('%', #{str}, '%') ORDER BY id DESC LIMIT #{start}, 10")
    ArrayList<WhiteBoard> searchList(@Param("start")int start, @Param("type")SearchType type, @Param("str")String str) throws Exception;
    @Select("SELECT count(*) FROM whiteboard WHERE isEnable = 1 AND ${type} LIKE CONCAT('%', #{str},'%')")
    int searchTotalCount(@Param("type")SearchType type, @Param("str")String str) throws Exception;
}
