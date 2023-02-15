package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.AccountService;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.ScriptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login")
    public String Login(@AuthenticationPrincipal CustomUserDetails userDetails){
        if (userDetails != null) {
            return "redirect:/";
        }
        return "Login";
    }

    @GetMapping(value = "/logout")
    public String Logout(HttpServletRequest request) {
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value = "/sign_up")
    public String Register(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request
            ,HttpServletResponse response, @RequestParam(required = false) String email
            , @RequestParam(required = false) String password, @RequestParam(required = false) String name) throws Exception {
        if (userDetails != null) {
            return "redirect:" + request.getHeader("referer");
        }
        if (email != null && password != null && name != null && !accountService.checkEmail(email)) {
            accountService.register(new AccountDTO(0, email, passwordEncoder.encode(password), name, "", null
                    , "USER", CurrentTime.getTime(), 1,0,0,true));
            ScriptUtils.alertAndMovePage(response, "회원가입 성공!", "/login");
        }
        return "Sign_Up";
    }
}
