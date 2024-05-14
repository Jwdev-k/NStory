package nk.service.NStory.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.config.RecaptchaConfig;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RecaptchaFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/perform_login")) {
            // 구글 reCAPTCHA v3 토큰을 요청 파라미터에서 가져옴
            String recaptchaToken = request.getParameter("token");

            if (recaptchaToken != null && !recaptchaToken.isEmpty()) {
                log.info("로그인 리캡챠 인증 절차 실행");
                try {
                    if (!RecaptchaConfig.verify(recaptchaToken)) {
                        request.setAttribute("errMsg", "비정상 접근 시도 차단");
                        request.getRequestDispatcher("/login").forward(request, response);
                        return;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
