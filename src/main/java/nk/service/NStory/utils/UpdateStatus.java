package nk.service.NStory.utils;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.dto.ExpTable;
import nk.service.NStory.repository.AccountMapper;
import nk.service.NStory.repository.ExpTableMapper;
import nk.service.NStory.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class UpdateStatus {
    private final ExpTableMapper expTableMapper;
    private final AccountMapper accountMapper;

    @Transactional
    public void addExp(int exp, String email, int currentLevel) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountMapper.UpdateExp(exp,email); // 경험치 추가
        ExpTable newLevel = expTableMapper.getExpRange(email);
        if (currentLevel != newLevel.getLevel()) {
            accountMapper.UpdateLevel(newLevel.getLevel(), email);
            accountMapper.UpdateCoin(100, email);
            userDetails.setLevel(newLevel.getLevel());
            userDetails.setNCoin(userDetails.getNCoin() + 100);
        }
        Authentication newAuth;
        if (userDetails.isOAuth()) { // OAuth 객체 Null이 아닌경우
            newAuth = new OAuth2AuthenticationToken(userDetails, userDetails.getAuthorities(), userDetails.getName());
        } else {
            newAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        }
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Transactional
    public void addNCoin(int count, String email) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountMapper.UpdateCoin(count, email);
        userDetails.setNCoin(userDetails.getNCoin() + count);
        Authentication newAuth;
        if (userDetails.isOAuth()) { // OAuth 객체 Null이 아닌경우
            newAuth = new OAuth2AuthenticationToken(userDetails, userDetails.getAuthorities(), userDetails.getName());
        } else {
            newAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        }
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Transactional
    public boolean checkingReward(AccountDTO account) throws Exception {
        boolean firstLogin = false;
            if (account.getLastDateTime() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate lastDate = LocalDate.parse(account.getLastDateTime().substring(0, 10), formatter);
                if (LocalDate.now().isAfter(lastDate)) {
                    firstLogin = true;
                    accountMapper.UpdateExp(100, account.getEmail());
                }
            }
            return firstLogin;
    }
}
