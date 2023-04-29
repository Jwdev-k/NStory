package nk.service.NStory.repository;

import nk.service.NStory.dto.Enum.SearchType;
import nk.service.NStory.dto.bbs.WhiteBoard;
import nk.service.NStory.dto.bbs.WhiteBoardList;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface WhiteBoardMapper {
    @Select("SELECT wb.id, wb.title, wb.author, wb.email, wb.creationDate, wb.views, wb.like_count, " +
            "(SELECT COUNT(*) FROM comment as cm WHERE cm.id = wb.id) + (SELECT COUNT(*) FROM reply as rp WHERE rp.id = wb.id) as cm_rp_counts " +
            "FROM whiteboard as wb WHERE wb.bid = #{bid} AND wb.isNotice = 0 AND wb.isEnable = 1 ORDER BY wb.id DESC LIMIT #{start}, 50")
    ArrayList<WhiteBoardList> boardList(@Param("bid") String bid, @Param("start") int start) throws Exception;
    @Select("SELECT * FROM whiteboard WHERE isEnable = 1 AND id = #{id}")
    WhiteBoard getBoardView(int id) throws Exception;
    @Select("SELECT count(*) FROM whiteboard WHERE bid = #{bid} AND isEnable = 1 AND isNotice = 0")
    int totalCount(String bid) throws Exception;
    @Insert("INSERT INTO whiteboard VALUE(null, #{bid}, #{title}, #{contents}, #{author}, #{email}, #{creationDate}, #{views}, #{like_count}, #{dislike_count}, #{isNotice}, #{isEnable})")
    void insertBoard(WhiteBoard wb) throws Exception;
    @Delete("DELETE FROM whiteboard WHERE id = #{id} AND email = #{email}")
    void deleteBoard(@Param("id") int id, @Param("email")String email) throws Exception;
    @Update("UPDATE whiteboard SET title = #{title}, contents = #{contents}, author = #{author}, isNotice = #{isNotice} WHERE id = #{id} AND email = #{email}")
    void updateBoard(WhiteBoard wb) throws Exception;
    @Select("SELECT wb.id, wb.title, wb.author, wb.email, wb.creationDate, wb.views, wb.like_count, " +
            "(SELECT COUNT(*) FROM comment as cm WHERE cm.id = wb.id) + (SELECT COUNT(*) FROM reply as rp WHERE rp.id = wb.id) as cm_rp_counts " +
            "FROM whiteboard as wb WHERE wb.bid = #{bid} AND wb.isNotice = 0 AND wb.isEnable = 1 AND ${'wb.' + type} LIKE CONCAT('%', #{str}, '%') ORDER BY wb.id DESC LIMIT #{start}, 50")
    ArrayList<WhiteBoardList> searchList(@Param("bid") String bid, @Param("start")int start, @Param("type")SearchType type, @Param("str")String str) throws Exception;
    @Select("SELECT count(*) FROM whiteboard WHERE bid = #{bid} AND isEnable = 1 AND isNotice = 0 AND ${type} LIKE CONCAT('%', #{str},'%')")
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
    @Select("SELECT wb.id, wb.title, wb.author, wb.email, wb.creationDate, wb.views, wb.like_count," +
            " (SELECT COUNT(*) FROM comment as cm WHERE cm.id = wb.id) + (SELECT COUNT(*) FROM reply as rp WHERE rp.id = wb.id) as cm_rp_counts" +
            " FROM whiteboard as wb WHERE wb.bid = #{bid} AND wb.isNotice = 1 AND wb.isEnable = 1 ORDER BY wb.id DESC LIMIT 10")
    ArrayList<WhiteBoardList> getNoticeList(String bid) throws Exception;
    @Select("SELECT wb.id, wb.title, wb.author, wb.email, wb.creationDate, wb.views, wb.like_count,\n" +
            "(SELECT COUNT(*) FROM comment as cm WHERE cm.id = wb.id) + (SELECT COUNT(*) FROM reply as rp WHERE rp.id = wb.id) as cm_rp_counts\n" +
            ", wl.kname, a.id as aid FROM whiteboard as wb INNER JOIN whiteboard_list as wl ON wl.bid = wb.bid" +
            " INNER JOIN account as a ON a.email = wb.email WHERE wb.isNotice = 0 AND wb.isEnable = 1 AND wb.like_count >= 10 ORDER by wb.id DESC LIMIT 6")
    ArrayList<WhiteBoardList> getBestList() throws Exception;
    @Select("SELECT wb.id, wb.title, wb.author, wb.email, wb.creationDate, wb.views, wb.like_count,\n" +
            "(SELECT COUNT(*) FROM comment as cm WHERE cm.id = wb.id) + (SELECT COUNT(*) FROM reply as rp WHERE rp.id = wb.id) as cm_rp_counts\n" +
            ", wl.kname, a.id as aid FROM whiteboard as wb INNER JOIN whiteboard_list as wl ON wl.bid = wb.bid" +
            " INNER JOIN account as a ON a.email = wb.email WHERE wb.isNotice = 0 AND wb.isEnable = 1 ORDER by wb.id DESC LIMIT 6")
    ArrayList<WhiteBoardList> getRecentList() throws Exception;
    @Select("SELECT wb.id, wb.title, wb.author, wb.email, wb.creationDate, wb.views, wb.like_count,\n" +
            "(SELECT COUNT(*) FROM comment as cm WHERE cm.id = wb.id) + (SELECT COUNT(*) FROM reply as rp WHERE rp.id = wb.id) as cm_rp_counts\n" +
            ", wl.kname, a.id as aid FROM whiteboard as wb INNER JOIN whiteboard_list as wl ON wl.bid = wb.bid" +
            " INNER JOIN account as a ON a.email = wb.email WHERE wb.isNotice = 1 AND wb.isEnable = 1 AND wb.bid = 'mainNotice' ORDER by wb.id DESC LIMIT 3")
    ArrayList<WhiteBoardList> getMainNoticeList() throws Exception;
}
