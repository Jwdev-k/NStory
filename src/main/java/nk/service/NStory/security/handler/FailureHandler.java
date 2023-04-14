package nk.service.NStory.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component @Slf4j
public class FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException exception) throws IOException, ServletException {
        log.info("Try Login IP : " + request.getRemoteHost());
        String errMsg;
        if (exception instanceof BadCredentialsException) {
            errMsg = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errMsg = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다.";
        } else if (exception instanceof UsernameNotFoundException) {
            errMsg = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errMsg = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        } else {
            errMsg = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
        }
        log.warn(errMsg); // 로그인 에러 오류 메세지
        request.setAttribute("errMsg", errMsg);
        request.getRequestDispatcher("/login").forward(request,response);
    }
}
