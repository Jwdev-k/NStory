package nk.service.NStory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RankingDTO {
    private int rank;
    private int id;
    private String name;
    private int level;
    private int exp;
    private int nCoin;
}
