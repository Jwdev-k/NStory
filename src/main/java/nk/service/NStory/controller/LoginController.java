package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
import nk.service.NStory.utils.RandomPassword;
import nk.service.NStory.utils.ScriptUtils;
import nk.service.NStory.utils.SecurityCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    public String Register(HttpServletRequest request, HttpSession session) {
        request.setAttribute("email", session.getAttribute("SignUpSession"));
        return "Sign_Up";
    }

    @PostMapping(value = "/sign_up")
    public String Register(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request
            , HttpServletResponse response, HttpSession session, @RequestParam String email, @RequestParam String password
            , @RequestParam String name, @RequestParam String token) throws Exception {
        String SessionEmail = session.getAttribute("SignUpSession").toString();
        if (userDetails != null) {
            return "redirect:" + request.getHeader("referer");
        } else if (email != null && password != null && name != null && name.length() >= 2 && !accountService.checkEmail(email)
                && RecaptchaConfig.verify(token) && SessionEmail.equals(email)) {
            accountService.register(new AccountDTO(0, email, passwordEncoder.encode(password), name, "", null
                    , "USER", CurrentTime.getTime(), null, 1, 0, 0, true, false));
            session.removeAttribute("SignUpSession");
            ScriptUtils.alertAndMovePage(response, "회원가입 성공!", "/login");
            mailService.sendEmail(new MailDTO(email, "NStory 회원가입에 성공했습니다."
                    , "가입해주셔서 감사합니다."));
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
            return "redirect:/check_email";
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
            , @ModelAttribute("email") String email, @ModelAttribute("isSignUp") boolean isSignUp) {
        if (userDetails != null || email.length() <= 0) {
            return "redirect:" + request.getHeader("referer");
        }
        return "checkCode";
    }

    @ResponseBody
    @PostMapping(value = "/check_code")
    public ResponseEntity<String> checkCode(HttpServletRequest request, HttpSession session
            , @RequestParam String email, @RequestParam String code
            , @RequestParam(required = false, defaultValue = "false") boolean isSignUp) throws Exception {
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
            } else if (!isSignUp) {
                securityCode.deleteSecurityCode(email, request);
                String randomPassword = new RandomPassword().generateRandomPassword();
                mailService.sendEmail(new MailDTO(email, "NStory 임시비밀번호 발급"
                        , String.format("임시비밀번호는 [%s] 입니다. \n 꼭 로그인 후 패스워드를 재설정 해주세요.", randomPassword)));
                responseJson.put("result", "success");
                responseJson.put("redirectUrl", "/login");
                responseJson.put("msg", "임시비밀번호가 발급되었습니다. \n 메일 확인 후 로그인 해주세요.");
                accountService.resetPassword(email, passwordEncoder.encode(randomPassword));
            } else {
                securityCode.deleteSecurityCode(email, request);
                responseJson.put("result", "signup");
                responseJson.put("redirectUrl", "/sign_up");
                session.setAttribute("SignUpSession", email);
            }
        }
        request.setAttribute("email", email);
        return ResponseEntity.ok(responseJson.toJSONString());
    }

    @GetMapping(value = "/new_pw")
    public String newPassword(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        if (userDetails != null && userDetails.getOAuth2UserInfo() == null) {
            return "UpdatePW";
        }
        return "redirect:" + request.getHeader("referer");
    }

    @PostMapping(value = "/new_pw")
    public String newPassword(HttpServletRequest request, HttpServletResponse response, Authentication auth
            , @RequestParam String email, @RequestParam String password, @RequestParam String password2) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        if (password.length() >= 6 && password.length() <= 16 && password.equals(password2) && userDetails.getEmail().equals(email)) {
            accountService.resetPassword(email, passwordEncoder.encode(password));
            new SecurityContextLogoutHandler().logout(request, response, auth);
            ScriptUtils.alertAndMovePage(response, "패스워드가 변경되었습니다. 다시 로그인 해주세요.", "/login");
            return null;
        } else {
            request.setAttribute("errMsg", "입력한 패스워드가 일치하지않습니다. 다시 입력해주세요.");
        }
        return "UpdatePW";
    }

    @GetMapping(value = "/check_email")
    public String checkEmail(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        if (userDetails != null) {
            return "redirect:" + request.getHeader("referer");
        }
        return "CheckEmail";
    }

    @PostMapping(value = "/check_email")
    public String checkEmailPost(HttpServletRequest request, @RequestParam String email
            , @RequestParam String token, RedirectAttributes redirectAttributes) throws Exception {
        if (email != null && token != null) {
            if (!accountService.checkEmail2(email) && RecaptchaConfig.verify(token)) {
                String generateCode = securityCode.generateSecurityCode();
                mailService.sendEmail(new MailDTO(email, "NStory 보안코드 메일입니다.", String.format("보안코드는 [%s] 입니다.", generateCode)));
                securityCode.saveSecurityCode(email, generateCode, request);

                redirectAttributes.addFlashAttribute("email", email);
                redirectAttributes.addFlashAttribute("isSignUp", true);
                return "redirect:/check_code";
            } else {
                request.setAttribute("error", "이미 가입된 이메일 입니다. 다시 시도 해주세요.");
            }
        }
        return "CheckEmail";
    }
}
