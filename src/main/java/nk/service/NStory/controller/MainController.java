package nk.service.NStory.controller;

import nk.service.NStory.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping(value = "/")
    public String main(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        boolean isLogin = customUserDetails != null;
        model.addAttribute("IsLogin", isLogin);
        return "Test";
    }
}
