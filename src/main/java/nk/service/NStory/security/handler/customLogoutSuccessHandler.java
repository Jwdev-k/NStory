package nk.service.NStory.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class customLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        int flag = Integer.parseInt(request.getParameter("flag"));
        // flag = 1 패스워드 변경페이지에서의 이동 / flag = 0 기본
        switch (flag) {
            case 0 -> response.sendRedirect(request.getHeader("referer"));
            case 1 -> response.sendRedirect("/login");
        }
    }
}
