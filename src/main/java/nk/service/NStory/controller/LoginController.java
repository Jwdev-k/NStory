package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.config.RecaptchaConfig;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.AccountService;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.ScriptUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller @Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final AccountService accountService;
    private final BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login")
    public String Login(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            return "redirect:/";
        }
        return "Login";
    }

    @GetMapping(value = "/logout")
    public String Logout(HttpServletRequest request) {
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping(value = "/sign_up")
    public String Register() {
        return "Sign_Up";
    }

    @PostMapping(value = "/sign_up")
    public String Register(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request
            , HttpServletResponse response, @RequestParam String email, @RequestParam String password
            , @RequestParam String name, @RequestParam String token) throws Exception {
        if (userDetails != null) {
            return "redirect:" + request.getHeader("referer");
        }
        else if (email != null && password != null && name != null && name.length() >= 2 && !accountService.checkEmail(email)
                && RecaptchaConfig.verify(token)) {
            accountService.register(new AccountDTO(0, email, passwordEncoder.encode(password), name, "", null
                    , "USER", CurrentTime.getTime(), null, 1, 0, 0, true));
            ScriptUtils.alertAndMovePage(response, "회원가입 성공!", "/login");
        }
        request.setAttribute("errMsg", "요청이 실패 하였습니다.");
        return "Sign_up";
    }

    @GetMapping(value = "/agree")
    public String agreeCheck() {
        return "Agree";
    }

    @PostMapping(value = "/agree")
    public String Register(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request,
                           @RequestParam(defaultValue = "false") boolean agree, @RequestParam String token) throws Exception {
        if (userDetails != null) {
            return "redirect:" + request.getHeader("referer");
        }
        else if (RecaptchaConfig.verify(token) && agree) {
            return "redirect:/sign_up";
        }
        request.setAttribute("errMsg", "이용약관에 필수로 동의하셔야합니다.");
        return "Agree";
    }

}
