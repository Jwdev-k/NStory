package nk.service.NStory.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.service.impl.AccountService;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import java.util.Collections;

public class CustomRememberMe extends TokenBasedRememberMeServices {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UpdateStatus updateStatus;

    public CustomRememberMe(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    private String createCustomRememberMeValue(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return user.getEmail() + ":" + user.getName();
    }

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication successfulAuthentication) {
        String value = createCustomRememberMeValue(successfulAuthentication);

        setCookie(new String[]{value}, getTokenValiditySeconds(), request, response);
    }

    @SneakyThrows
    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request
            , HttpServletResponse response) throws RememberMeAuthenticationException {
        // 저장된 쿠키에서 사용자 이름 대신 값을 읽어옵니다.
        String customValue = cookieTokens[0];

        // 저장된 값을 파싱하여 사용자 이름(username)과 프로바이더(provider)를 추출합니다.
        String[] tokens = customValue.split(":");
        String email = tokens[0];

        AccountDTO account = accountService.login(email);
        if (account != null) {
            accountService.UpdateLastLoginDate(CurrentTime.getTime(), email);
            return new CustomUserDetails(account.getId(), account.getName(), account.getEmail(), account.getPassword()
                    , account.getComment(), account.getLevel(), account.getExp(), account.getNCoin(), account.isEnable()
                    , Collections.singleton(new SimpleGrantedAuthority("ROLE_" + account.getRole()))
                    , updateStatus.checkingReward(account), account.isOAuth());
        } else {
            throw new UsernameNotFoundException(email + " 해당 이메일 존재 하지 않음.");
        }
    }
}
