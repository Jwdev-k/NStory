package nk.service.NStory.service.impl;

import lombok.SneakyThrows;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Service
public class UserLoginService implements UserDetailsService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UpdateStatus updateStatus;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username)  {
        AccountDTO account = accountService.login(username);
        boolean firstLogin = false;
        if (account != null) {
            if (!account.isEnable()) {
                throw new AuthenticationCredentialsNotFoundException("인증 요청 거부 (계정 비활성화 상태)");
            } else {
                if (account.getLastDateTime() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate lastDate = LocalDate.parse(account.getLastDateTime().substring(0, 10), formatter);
                    if (LocalDate.now().isAfter(lastDate)) {
                        firstLogin = true;
                        updateStatus.addExp(100, username, account.getLevel());
                    }
                }
                accountService.UpdateLastLoginDate(CurrentTime.getTime(), username);
                return new CustomUserDetails(account.getName(), account.getEmail(), account.getPassword()
                        , account.getComment(), account.getProfileImg(), account.isEnable()
                        , true, true, true
                        , Collections.singleton(new SimpleGrantedAuthority("ROLE_" + account.getRole()))
                        , firstLogin);
            }
        } else {
            throw new UsernameNotFoundException(username + " 해당 이메일 존재 하지 않음.");
        }
    }
}
