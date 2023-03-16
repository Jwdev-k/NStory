package nk.service.NStory.repository;

import nk.service.NStory.dto.RankingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface RankMapper {
    @Select("SELECT ROW_NUMBER() OVER (ORDER BY level DESC) as rank, email, name, level, exp, nCoin FROM account LIMIT #{start}, 20")
    ArrayList<RankingDTO> LevelRankingList(int start) throws Exception;
    @Select("SELECT ROW_NUMBER() OVER (ORDER BY exp DESC) as rank, email, name, level, exp, nCoin FROM account LIMIT #{start}, 20")
    ArrayList<RankingDTO> ExpRankingList(int start) throws Exception;
    @Select("SELECT ROW_NUMBER() OVER (ORDER BY nCoin DESC) as rank, email, name, level, exp, nCoin FROM account LIMIT #{start}, 20")
    ArrayList<RankingDTO> nCoinRankingList(int start) throws Exception;
    @Select("SELECT count(*) FROM account")
    int totalCount() throws Exception;
}
