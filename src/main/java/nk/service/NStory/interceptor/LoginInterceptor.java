package nk.service.NStory.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
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
        request.setAttribute("isLogin", value);
    }
}
