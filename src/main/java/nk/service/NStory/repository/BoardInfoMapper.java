package nk.service.NStory.repository;

import nk.service.NStory.dto.bbs.BoardInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface BoardInfoMapper {
    @Select("SELECT wl.*, a.name as username FROM whiteboard_list wl INNER JOIN account a ON a.email = wl.email WHERE bid = #{bid}")
    BoardInfo getBoardInfo(String bid) throws Exception;
    @Select("SELECT bid, kname FROM whiteboard_list WHERE bid != 'mainNotice'")
    ArrayList<BoardInfo> getBoardNameList() throws Exception;
}
