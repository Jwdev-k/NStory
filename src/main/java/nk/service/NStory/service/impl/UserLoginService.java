package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.UpdateStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserLoginService implements UserDetailsService {
    private final AccountService accountService;
    private final UpdateStatus updateStatus;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) {
        AccountDTO account = accountService.login(email);
        if (account != null) {
            if (!account.isEnable() || account.isOAuth()) {
                throw new AuthenticationCredentialsNotFoundException("인증 요청 거부 (계정 비활성화 상태)");
            } else {
                accountService.UpdateLastLoginDate(CurrentTime.getTime(), email);
                return new CustomUserDetails(account.getName(), account.getEmail(), account.getPassword()
                        , account.getComment(), account.getProfileImg(), account.getLevel(), account.getExp()
                        , account.getNCoin(), account.isEnable()
                        , Collections.singleton(new SimpleGrantedAuthority("ROLE_" + "USER"))
                        , updateStatus.checkingReward(account), account.isOAuth());
            }
        } else {
            throw new UsernameNotFoundException(email + " 해당 이메일 존재 하지 않음.");
        }
    }
}
