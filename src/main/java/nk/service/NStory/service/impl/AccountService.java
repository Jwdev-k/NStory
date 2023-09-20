package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.dto.ByteImageDTO;
import nk.service.NStory.repository.AccountMapper;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.AccountServiceIF;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service @Slf4j
@RequiredArgsConstructor
public class AccountService implements AccountServiceIF {
    private final AccountMapper accountMapper;

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

    @Override
    public boolean checkEmail2(String email) throws Exception {
        return accountMapper.checkEmail2(email);
    }

    @Transactional
    @Override
    public void UpdateLastLoginDate(String lastLoginDate, String email) throws Exception {
        accountMapper.UpdateLastLoginDate(lastLoginDate,email);
    }

    @Transactional
    @Override
    public void UpdateAccountInfo(AccountDTO accountDTO) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (accountDTO.getProfileImg() != null && accountDTO.getProfileImg().length > 0) { // 프로필 정보 업데이트
            accountMapper.UpdateAccountInfo(accountDTO);
        } else {
            accountMapper.UpdateAccountInfo2(accountDTO);
            userDetails.setUsername(accountDTO.getName());
            userDetails.setComment(accountDTO.getComment());
        }
        Authentication newAuth;
        if (userDetails.isOAuth()) { // OAuth 계정 인 경우
            newAuth = new OAuth2AuthenticationToken(userDetails, userDetails.getAuthorities(),
                    userDetails.getOAuth2UserInfo() != null ? userDetails.getName() : "RememberMe");
        } else {
            newAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        }
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Transactional
    @Override
    public void resetPassword(String email, String password) throws Exception {
        accountMapper.resetPassword(email, password);
        log.info("패스워드 변경 이력{이메일: " + email + ",변경패스워드: " + password + "}");
    }

    @Override
    public ByteImageDTO getUserImage(int id) throws Exception {
        return accountMapper.getUserImage(id);
    }

    @Transactional
    @Override
    public void deleteAccount(int aid) throws Exception {
        accountMapper.deleteAccount(aid);
        log.info("번호 : " + aid + "계정이 회원탈퇴 처리 되었습니다.");
    }
}
