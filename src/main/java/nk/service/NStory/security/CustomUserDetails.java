package nk.service.NStory.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nk.service.NStory.dto.oauth2.OAuth2UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serial;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
@Getter @Setter
public class CustomUserDetails implements UserDetails, OAuth2User {
    @Serial
    private static final long serialVersionUID = -6613434008874770290L;

    // OAuth2 로그인용
    private OAuth2UserInfo oAuth2UserInfo;

    // 공통 필수 정보
    private int aid;
    private String username;
    private String password;
    private String email;
    private String comment;
    private int level;
    private int exp;
    private int nCoin;
    private boolean isEnabled; // 계정 활성화 여부
    private boolean isAccountNonExpired = true; //계정 만료 여부
    private boolean isAccountNonLocked = true; //계정 잠김 여부
    private boolean isCredentialsNonExpired = true; // 계정 비밀번호 만료 여부
    private Collection<? extends GrantedAuthority> authorities;// 권한 목록
    private boolean firstLogin;
    private boolean isOAuth;

    // 자동로그인용
    private String providerName = "nstory";

    //Login
    public CustomUserDetails(int aid, String username ,String email, String password, String comment
            , int level, int exp, int nCoin, boolean isEnabled, Collection<? extends GrantedAuthority> authorities
            , boolean firstLogin, boolean isOAuth) {
        this.aid = aid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.comment = comment;
        this.level = level;
        this.exp = exp;
        this.nCoin = nCoin;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
        this.firstLogin = firstLogin;
        this.isOAuth = isOAuth;
    }

    //OAuth2 로그인
    public CustomUserDetails(OAuth2UserInfo attributes, int aid, String username, String email, String password, String comment
            , int level, int exp, int nCoin, boolean isEnabled, Collection<? extends GrantedAuthority> authorities
            , boolean firstLogin, boolean isOAuth) {
        this.oAuth2UserInfo = attributes;
        this.aid = aid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.comment = comment;
        this.level = level;
        this.exp = exp;
        this.nCoin = nCoin;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
        this.firstLogin = firstLogin;
        this.isOAuth = isOAuth;
    }

    // 자동로그인
    public CustomUserDetails(int aid, String username ,String email, String password, String comment
            , int level, int exp, int nCoin, boolean isEnabled, Collection<? extends GrantedAuthority> authorities
            , boolean firstLogin, boolean isOAuth, String providerName) {
        this.aid = aid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.comment = comment;
        this.level = level;
        this.exp = exp;
        this.nCoin = nCoin;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
        this.firstLogin = firstLogin;
        this.isOAuth = isOAuth;
        this.providerName = providerName;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfo.getAttributes();
    }

    @Override
    public String getName() { // 로그인 출처
        return oAuth2UserInfo != null ? oAuth2UserInfo.getProvider() : providerName;
    }

    @Override
    public String toString() {
        return "\nCustomUserDetails{" +
                " username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", comment='" + comment + '\'' +
                ", isEnabled=" + isEnabled +
                ", isAccountNonExpired=" + isAccountNonExpired +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isCredentialsNonExpired=" + isCredentialsNonExpired +
                ", authorities=" + authorities +
                ", firstLogin=" + firstLogin +
                ", ProviderName=" + getName() +
                '}';
    }
}
