package nk.service.NStory.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.repository.LoginMapper;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.LoginServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.regex.Pattern;

@Service @Slf4j
public class LoginService implements UserDetailsService, LoginServiceIF {
    @Autowired
    private LoginMapper loginMapper;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username)  {
        AccountDTO account = loginMapper.login(username);
        if (account != null) {
            if (!account.isEnable()) {
                throw new AuthenticationCredentialsNotFoundException("인증 요청 거부 (계정 비활성화 상태)");
            }
            return new CustomUserDetails(account.getName(), account.getEmail(), account.getPassword()
                    , account.isEnable(), true, true, true
                    , Collections.singleton(new SimpleGrantedAuthority("ROLE_" + account.getRole())));
        } else {
            throw new UsernameNotFoundException(username + " 해당 이메일 아이디 NULL");
        }
    }

    @Transactional
    @Override
    public void register(AccountDTO accountDTO) throws Exception {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        boolean emailType = Pattern.matches(regex, accountDTO.getEmail());
        if (emailType) {
            boolean result = loginMapper.register(accountDTO);
            if (result) {
                log.info("계정 등록 성공");
            } else {
                log.info("계정 등록 실패");
            }
        }
    }

    @Override
    public boolean checkEmail(String email) throws Exception {
        return loginMapper.checkEmail(email);
    }

    @Override
    public void findPassword(String email) throws Exception {

    }
}
