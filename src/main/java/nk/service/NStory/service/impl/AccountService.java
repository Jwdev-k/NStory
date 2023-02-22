package nk.service.NStory.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.repository.AccountMapper;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.AccountServiceIF;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.regex.Pattern;

@Service @Slf4j
public class AccountService implements UserDetailsService, AccountServiceIF {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UpdateStatus updateStatus;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username)  {
        AccountDTO account = accountMapper.login(username);
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
                UpdateLastLoginDate(CurrentTime.getTime(), username);
                return new CustomUserDetails(account.getName(), account.getEmail(), account.getPassword()
                        , account.isEnable(), true, true, true
                        , Collections.singleton(new SimpleGrantedAuthority("ROLE_" + account.getRole())), firstLogin);
            }
        } else {
            throw new UsernameNotFoundException(username + " 해당 이메일 존재 하지 않음.");
        }
    }

    @Transactional
    @Override
    public void register(AccountDTO accountDTO) throws Exception {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        boolean emailType = Pattern.matches(regex, accountDTO.getEmail());
        if (emailType) {
            boolean result = accountMapper.register(accountDTO);
            if (result) {
                log.info("계정 등록 성공");
            } else {
                log.info("계정 등록 실패");
            }
        }
    }

    @Override
    public boolean checkEmail(String email) throws Exception {
        return accountMapper.checkEmail(email);
    }

    @Transactional
    @Override
    public void UpdateLastLoginDate(String lastLoginDate, String email) throws Exception {
        accountMapper.UpdateLastLoginDate(lastLoginDate,email);
    }

    @Override
    public void findPassword(String email) throws Exception {

    }
}
