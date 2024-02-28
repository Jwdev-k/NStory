package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.service.impl.RankingService;
import nk.service.NStory.utils.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RankingController {
    private final RankingService rankingService;
    private final PageUtil pageUtil = new PageUtil();

    @RequestMapping(value = "/ranking")
    public String RankingMain(Model model, HttpServletRequest request
            , @RequestParam(name = "page", required = false, defaultValue = "1") int page
            , @RequestParam(name = "search", required = false) String search) throws Exception {
        int totalCount;
        boolean isSearch;
        if (search != null && search.length() > 0) {
            model.addAttribute("rankingList", rankingService.ExpRankNameSerach(page, search));
            pageUtil.setPage(page);
            totalCount = rankingService.searchTotalCount(search);
            request.setAttribute("str", search);
            isSearch = true;
        } else {
            model.addAttribute("rankingList", rankingService.ExpRankingList(page));
            pageUtil.setPage(page);
            totalCount = rankingService.totalCount();
            isSearch = false;
        }
        if (totalCount > 0) {
            pageUtil.setTotalCount(totalCount);
        } else {
            pageUtil.setTotalCount(1);
        }
        model.addAttribute("pageMaker", pageUtil);
        model.addAttribute("crtPage", page);
        model.addAttribute("isSearch", isSearch);
        return "Ranking";
    }
}
