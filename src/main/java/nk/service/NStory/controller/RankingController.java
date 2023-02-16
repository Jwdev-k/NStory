package nk.service.NStory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RankingController {

    @RequestMapping(value = "/ranking")
    public String RankingMain() {
        return "Ranking";
    }
}
