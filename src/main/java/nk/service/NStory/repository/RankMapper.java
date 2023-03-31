package nk.service.NStory.repository;

import nk.service.NStory.dto.ByteImageDTO;
import nk.service.NStory.dto.RankingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface RankMapper {
    @Select("SELECT ROW_NUMBER() OVER (ORDER BY level DESC) as rank, id, name, level, exp, nCoin FROM account LIMIT #{start}, 20")
    ArrayList<RankingDTO> LevelRankingList(int start) throws Exception;
    @Select("SELECT ROW_NUMBER() OVER (ORDER BY exp DESC) as rank, id, name, level, exp, nCoin FROM account LIMIT #{start}, 20")
    ArrayList<RankingDTO> ExpRankingList(int start) throws Exception;
    @Select("SELECT ROW_NUMBER() OVER (ORDER BY nCoin DESC) as rank, id, name, level, exp, nCoin FROM account LIMIT #{start}, 20")
    ArrayList<RankingDTO> nCoinRankingList(int start) throws Exception;
    @Select("SELECT count(*) FROM account")
    int totalCount() throws Exception;
    @Select("SELECT profileImg FROM account WHERE id = #{id}")
    ByteImageDTO getUserImage(int id) throws Exception;
    @Select("select r.* from (SELECT ROW_NUMBER() OVER (ORDER BY exp DESC) as rank, id, name, level, exp, nCoin FROM account LIMIT #{start}, 20)" +
            " as r where name LIKE CONCAT('%', #{name},'%')")
    ArrayList<RankingDTO> ExpRankNameSerach(@Param("start")int start, @Param("name")String name) throws Exception;
    @Select("SELECT count(*) FROM account WHERE name LIKE CONCAT('%', #{name},'%')")
    int searchTotalCount(String name) throws Exception;
}
