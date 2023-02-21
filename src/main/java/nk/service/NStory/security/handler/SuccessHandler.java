package nk.service.NStory.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.utils.ScriptUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component @Slf4j
public class SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("Try Login IP : " + request.getRemoteHost() + "\n"
                + userDetails.getEmail() + ": Login Success");
        if (userDetails.isFirstLogin()) {
            ScriptUtils.alertAndMovePage(response, "첫 로그인 보너스 경험치. +100이 지급 되었습니다.", "/");
        } else {
            response.sendRedirect("/");
        }
    }
}
