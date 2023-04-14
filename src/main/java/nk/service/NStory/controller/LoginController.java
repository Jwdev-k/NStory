package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import nk.service.NStory.config.RecaptchaConfig;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.dto.MailDTO;
import nk.service.NStory.dto.SecurityCodeDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.AccountService;
import nk.service.NStory.service.impl.MailService;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.ScriptUtils;
import nk.service.NStory.utils.SecurityCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final AccountService accountService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final SecurityCode securityCode;

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
        } else if (email != null && password != null && name != null && name.length() >= 2 && !accountService.checkEmail(email)
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
                           @RequestParam(defaultValue = "false") boolean agree, @RequestParam(required = false) String token) throws Exception {
        if (userDetails != null) {
            return "redirect:" + request.getHeader("referer");
        } else if (RecaptchaConfig.verify(token) && agree) {
            return "redirect:/sign_up";
        }
        request.setAttribute("errMsg", "이용약관에 필수로 동의하셔야합니다.");
        return "Agree";
    }

    @GetMapping(value = "/findpw")
    public String findPassword(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        if (userDetails != null) {
            return "redirect:" + request.getHeader("referer");
        }
        return "FindPassword";
    }

    @PostMapping(value = "/findpw")
    public String findPassword(HttpServletRequest request, @RequestParam String email
            , @RequestParam String token, RedirectAttributes redirectAttributes) throws Exception {
        if (email != null && token != null) {
            if (accountService.checkEmail(email) && RecaptchaConfig.verify(token)) { // 메일발송
                String generateCode = securityCode.generateSecurityCode();
                mailService.sendEmail(new MailDTO(email, "NStory 보안코드 메일입니다.", String.format("보안코드는 [%s] 입니다.", generateCode)));
                securityCode.saveSecurityCode(email, generateCode, request);

                redirectAttributes.addFlashAttribute("email", email);
                return "redirect:/check_code";
            } else {
                request.setAttribute("error", "유효하지 않습니다. 다시 시도 해주세요.");
            }
        }
        return "FindPassword";
    }

    @GetMapping(value = "/check_code")
    public String checkCode(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request
            , @ModelAttribute("email") String email) {
        if (userDetails != null || email.length() <= 0) {
            return "redirect:" + request.getHeader("referer");
        }
        return "checkCode";
    }

    @ResponseBody
    @PostMapping(value = "/check_code")
    public ResponseEntity<String> checkCode(HttpServletRequest request, @RequestParam String email
            , @RequestParam String code) throws Exception {
        JSONObject responseJson = new JSONObject();
        if (code != null && email != null) {
            SecurityCodeDTO codeDTO = securityCode.getSecurityCode(email, request);
            if ((System.currentTimeMillis() - codeDTO.getTime()) > (5 * 60 * 1000)) {
                securityCode.deleteSecurityCode(email, request);
                responseJson.put("result", "expired");
                responseJson.put("redirectUrl", "/findpw");
                responseJson.put("error", "보안코드가 만료되었습니다. 다시 시도 해주세요.");
            } else if (!code.equals(codeDTO.getCode())) {
                responseJson.put("result", "failed");
                responseJson.put("error", "보안코드가 틀렸습니다. 다시 확인 해주세요.");
            } else {
                responseJson.put("result", "success");
                responseJson.put("redirectUrl", "/login");
                securityCode.deleteSecurityCode(email, request);
            }
        }
        request.setAttribute("email", email);
        return ResponseEntity.ok(responseJson.toJSONString());
    }

}
