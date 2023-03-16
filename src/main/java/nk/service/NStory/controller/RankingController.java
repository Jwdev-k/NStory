package nk.service.NStory.controller;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.service.impl.RankingService;
import nk.service.NStory.utils.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RankingController {
    private final RankingService rankingService;
    private static final PageUtil pageUtil = new PageUtil();

    @RequestMapping(value = "/ranking")
    public String RankingMain(Model model, @RequestParam(required = false, defaultValue = "1") int page) throws Exception {
        model.addAttribute("rankingList", rankingService.LevelRankingList(page));
        pageUtil.setPage(page);
        pageUtil.setTotalCount(rankingService.totalCount());
        model.addAttribute("pageMaker", pageUtil);
        model.addAttribute("crtPage", page);
        return "Ranking";
    }
}
