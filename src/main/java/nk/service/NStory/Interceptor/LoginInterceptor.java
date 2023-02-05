package nk.service.NStory.Interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component @Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean value = false; // 로그인 상태 여부
        Cookie[] clientCookie =  request.getCookies();
        if (clientCookie != null) {
            for (Cookie id : clientCookie) {
                if (id.getName().equals("JSESSIONID") && request.getUserPrincipal() != null) {
                    value = true;
                    break;
                }
            }
        }
        return !value;
    }
}
