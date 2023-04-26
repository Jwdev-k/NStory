package nk.service.NStory.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.service.impl.BoardInfoService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component @Slf4j
@RequiredArgsConstructor
public class GlobalInterceptor implements HandlerInterceptor {
    private final BoardInfoService boardInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("boardInfoList", boardInfoService.getBoardNameList());
        String url = request.getRequestURI();

        // 리소스 요청인 경우 로그 생성하지 않음
        if(url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith(".png")
                || url.endsWith(".svg") || url.endsWith(".woff2")) {
            return true;
        }
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || "".equals(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        log.info("접근요청 Client IP: {}", clientIp);

        return true;
    }
}
