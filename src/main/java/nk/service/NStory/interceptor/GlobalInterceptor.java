package nk.service.NStory.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nk.service.NStory.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getUserPrincipal() != null) { // 세큐리티 유저 객체 가져오기
            SecurityContext context = SecurityContextHolder.getContext();
            CustomUserDetails userDetails = (CustomUserDetails) context.getAuthentication().getPrincipal();
            if (userDetails != null) {
                request.setAttribute("account", userDetails);
            }
        }
        return true;
    }
}
