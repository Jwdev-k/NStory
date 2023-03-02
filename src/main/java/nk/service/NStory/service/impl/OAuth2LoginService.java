package nk.service.NStory.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.dto.oauth2.GoogleUserInfo;
import nk.service.NStory.dto.oauth2.KakaoUserInfo;
import nk.service.NStory.dto.oauth2.NaverUserInfo;
import nk.service.NStory.dto.oauth2.OAuth2UserInfo;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Service @Slf4j
public class OAuth2LoginService extends DefaultOAuth2UserService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UpdateStatus updateStatus;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @SneakyThrows
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId();

        switch (provider) {
            case "naver" -> oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
            case "kakao" -> oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            case "google" -> oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        if (oAuth2UserInfo != null) {
            log.info(oAuth2UserInfo.toString());
            String email = oAuth2UserInfo.getEmail();
            String name = oAuth2UserInfo.getName();
            boolean firstLogin = false;
            AccountDTO account = accountService.login(email);
            if (account == null) {
                accountService.register(new AccountDTO(0, email, passwordEncoder.encode(provider), name,
                        null, null, "USER", CurrentTime.getTime(), null,
                        1, 0, 0, true));
            } else {
                if (account.getLastDateTime() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate lastDate = LocalDate.parse(account.getLastDateTime().substring(0, 10), formatter);
                    if (LocalDate.now().isAfter(lastDate)) {
                        firstLogin = true;
                        updateStatus.addExp(100, account.getEmail(), account.getLevel());
                    }
                }
                accountService.UpdateLastLoginDate(CurrentTime.getTime(), account.getEmail());
                return new CustomUserDetails(oAuth2UserInfo, account.getName(), account.getEmail(), passwordEncoder.encode(provider),
                        account.getComment(), account.getProfileImg(),
                        account.isEnable(), true, true, true
                        , Collections.singleton(new SimpleGrantedAuthority("ROLE_" + account.getRole())), firstLogin);
            }
            return new CustomUserDetails(oAuth2UserInfo, name, email, passwordEncoder.encode(provider), null
                    , null, true, true, true, true
                    , Collections.singleton(new SimpleGrantedAuthority("ROLE_" + "USER")), firstLogin);
        }
        return null;
    }
}
