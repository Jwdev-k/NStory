package nk.service.NStory.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.utils.ScriptUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import java.io.IOException;

@Slf4j
public class SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenBasedRememberMeServices rememberMeServices;

    public SuccessHandler(TokenBasedRememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("Try Login IP : " + request.getRemoteHost() + "\n" + userDetails.getEmail() + ": Login Success");

        String requestURL = request.getRequestURL().append("?").append(request.getQueryString()).toString();
        String requestURI = request.getRequestURI();

/*        if (userDetails.isOAuth()) {
            // 소셜로그인인 경우 강제 자동로그인 쿠키 생성
            rememberMeServices.onLoginSuccess(request, response, authentication);
        }*/

        boolean isLoginPage = requestURI.equals("/") || requestURI.equals("/perform_login")
                || requestURI.contains("/login/oauth2/code/");
        if (userDetails.isFirstLogin()) {
            // 첫 로그인 보너스 경험치 지급
            String alertMsg = "첫 로그인 보너스 경험치. +100이 지급되었습니다.";
            if (isLoginPage) {
                ScriptUtils.alertAndMovePage(response, alertMsg, "/");
            } else {
                ScriptUtils.alertAndMovePage(response, alertMsg, requestURL);
            }
        } else {
            // 일반적인 로그인 시 처리
            if (isLoginPage) {
                response.sendRedirect("/");
            } else {
                response.sendRedirect(requestURL);
            }
        }
    }
}
