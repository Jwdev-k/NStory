package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.ByteImageDTO;
import nk.service.NStory.service.impl.RankingService;
import nk.service.NStory.utils.PageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RankingController {
    private final RankingService rankingService;
    private final PageUtil pageUtil = new PageUtil();

    @RequestMapping(value = "/ranking")
    public String RankingMain(Model model, HttpServletRequest request
            , @RequestParam(required = false, defaultValue = "1") int page
            , @RequestParam(required = false) String search) throws Exception {
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

    @GetMapping(value = "/ranking/prf/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getProfileImage(HttpServletRequest request, @PathVariable int id) throws Exception {
        ByteImageDTO ImageByteArray = rankingService.getUserImage(id);
        if (ImageByteArray != null && ImageByteArray.getImage().length > 0) {
            return new ResponseEntity<>(ImageByteArray.getImage(), HttpStatus.OK);
        }
        File defaultImg = ResourceUtils.getFile("classpath:static/images/default_profileImg.png");
        FileInputStream in = new FileInputStream(defaultImg);
        return new ResponseEntity<>(in.readAllBytes(), HttpStatus.OK);
    }
}
