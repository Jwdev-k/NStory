package nk.service.NStory.service.impl;

import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.repository.AccountMapper;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.AccountServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service @Slf4j
public class AccountService implements AccountServiceIF {
    @Autowired
    private AccountMapper accountMapper;

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
    public AccountDTO login(String email) throws Exception {
        return accountMapper.login(email);
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

    @Transactional
    @Override
    public void UpdateAccountInfo(AccountDTO accountDTO, Authentication authentication) throws Exception {
        accountMapper.UpdateAccountInfo(accountDTO);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Authentication auth = null;
        if (userDetails.getOAuth2UserInfo() != null) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            auth = new OAuth2AuthenticationToken(
                    new CustomUserDetails(userDetails.getOAuth2UserInfo(), accountDTO.getName(),
                            userDetails.getEmail(), userDetails.getPassword(),  userDetails.isEnabled(),
                            true, true, true,
                            userDetails.getAuthorities(), userDetails.isFirstLogin())
                    , oauthToken.getAuthorities()
                    , oauthToken.getAuthorizedClientRegistrationId());
        } else {
            auth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(), authentication.getCredentials(), authentication.getAuthorities());
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public void findPassword(String email) throws Exception {

    }
}
