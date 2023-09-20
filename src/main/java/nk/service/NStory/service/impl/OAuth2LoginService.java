package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginService extends DefaultOAuth2UserService {
    private final AccountService accountService;
    private final UpdateStatus updateStatus;
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

            AccountDTO account = accountService.login(email);
            if (account == null) {
                accountService.register(new AccountDTO(0, email, passwordEncoder.encode(provider), name,
                        null, null, "USER", CurrentTime.getTime(), CurrentTime.getTime(),
                        1, 0, 0, true, true));
                AccountDTO account2 = accountService.login(email);
                return new CustomUserDetails(oAuth2UserInfo, account2.getId(), account2.getName(), account2.getEmail()
                        , passwordEncoder.encode(provider), account2.getComment()
                        , account2.getLevel(), account2.getExp(), account2.getNCoin(), account2.isEnable()
                        , Collections.singleton(new SimpleGrantedAuthority("ROLE_" + "USER"))
                        , updateStatus.checkingReward(account2), account2.isOAuth(), userRequest.getAccessToken().getTokenValue(), provider);
            } else {
                accountService.UpdateLastLoginDate(CurrentTime.getTime(), email);
                return new CustomUserDetails(oAuth2UserInfo, account.getId(), account.getName(), account.getEmail()
                        , passwordEncoder.encode(provider), account.getComment()
                        , account.getLevel(), account.getExp(), account.getNCoin(), account.isEnable()
                        , Collections.singleton(new SimpleGrantedAuthority("ROLE_" + account.getRole()))
                        , updateStatus.checkingReward(account), account.isOAuth(), userRequest.getAccessToken().getTokenValue(), provider);
            }
        }
        throw new OAuth2AuthenticationException("인증 정보가 없음");
    }
}
