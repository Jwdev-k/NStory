package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/")
    public String mainPage() {
        return "main";
    }

    @PostMapping(value = "/account/profile_update")
    public String UpdateAccountInfo(HttpServletRequest request, @RequestParam String nickname, @RequestParam String comment
            , @RequestParam MultipartFile profileImg, Authentication authentication) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        accountService.UpdateAccountInfo(new AccountDTO(userDetails.getEmail(), nickname, comment,
                profileImg != null ? profileImg.getBytes() : null), authentication);
        return "redirect:" + request.getHeader("referer");
    }
}
