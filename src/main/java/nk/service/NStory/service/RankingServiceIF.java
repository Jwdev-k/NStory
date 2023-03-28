package nk.service.NStory.service;

import nk.service.NStory.dto.ByteImageDAO;
import nk.service.NStory.dto.RankingDTO;

import java.util.ArrayList;

public interface RankingServiceIF {
    ArrayList<RankingDTO> LevelRankingList(int start) throws Exception;
    ArrayList<RankingDTO> ExpRankingList(int start) throws Exception;
    ArrayList<RankingDTO> nCoinRankingList(int start) throws Exception;
    int totalCount() throws Exception;
    ByteImageDAO getUserImage(int id) throws Exception;
    ArrayList<RankingDTO> ExpRankNameSerach(int start, String name) throws Exception;
    int searchTotalCount(String name) throws Exception;
}
