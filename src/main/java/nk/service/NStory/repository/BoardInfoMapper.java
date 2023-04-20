package nk.service.NStory.repository;

import nk.service.NStory.dto.BoardInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface BoardInfoMapper {
    @Select("SELECT * FROM whiteboard_list WHERE bid = #{bid} ORDER BY kname ASC")
    BoardInfo getBoardInfo(String bid) throws Exception;
    @Select("SELECT bid, kname FROM whiteboard_list")
    ArrayList<BoardInfo> getBoardNameList() throws Exception;
}
