package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.ByteImageDTO;
import nk.service.NStory.dto.RankingDTO;
import nk.service.NStory.repository.RankMapper;
import nk.service.NStory.service.RankingServiceIF;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RankingService implements RankingServiceIF {
    private final RankMapper rankMapper;

    @Override
    public ArrayList<RankingDTO> LevelRankingList(int start) throws Exception {
        if (start == 1) {
            return rankMapper.LevelRankingList(0);
        } else {
            return rankMapper.LevelRankingList((start - 1) * 20);
        }
    }

    @Override
    public ArrayList<RankingDTO> ExpRankingList(int start) throws Exception {
        if (start == 1) {
            return rankMapper.ExpRankingList(0);
        } else {
            return rankMapper.ExpRankingList((start - 1) * 20);
        }
    }

    @Override
    public ArrayList<RankingDTO> nCoinRankingList(int start) throws Exception {
        if (start == 1) {
            return rankMapper.nCoinRankingList(0);
        } else {
            return rankMapper.nCoinRankingList((start - 1) * 20);
        }
    }

    @Override
    public int totalCount() throws Exception {
        return rankMapper.totalCount();
    }

    @Override
    public ByteImageDTO getUserImage(int id) throws Exception {
        return rankMapper.getUserImage(id);
    }

    // 랭킹테이블 검색기능
    @Override
    public ArrayList<RankingDTO> ExpRankNameSerach(int start, String name) throws Exception {
        if (start == 1) {
            return rankMapper.ExpRankNameSerach(0, name);
        } else {
            return rankMapper.ExpRankNameSerach((start - 1) * 20, name);
        }
    }

    @Override
    public int searchTotalCount() throws Exception {
        return rankMapper.totalCount();
    }
}
