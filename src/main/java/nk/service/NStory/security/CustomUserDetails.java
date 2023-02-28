package nk.service.NStory.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import nk.service.NStory.dto.oauth2.OAuth2UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serial;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
@Getter @ToString
public class CustomUserDetails implements UserDetails, OAuth2User {
    @Serial
    private static final long serialVersionUID = -6613434008874770290L;

    private OAuth2UserInfo oAuth2UserInfo;

    private String username;
    private String password;
    private String email;
    private boolean isEnabled; // 계정 활성화 여부
    private boolean isAccountNonExpired; //계정 만료 여부
    private boolean isAccountNonLocked; //계정 잠김 여부
    private boolean isCredentialsNonExpired; // 계정 비밀번호 만료 여부
    private Collection<? extends GrantedAuthority> authorities;// 권한 목록
    private boolean firstLogin;

    //Login
    public CustomUserDetails(String username, String email, String password, boolean isEnabled
            , boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired
            , Collection<? extends GrantedAuthority> authorities, boolean firstLogin) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.authorities = authorities;
        this.firstLogin = firstLogin;
    }

    public CustomUserDetails(OAuth2UserInfo attributes, String username, String email, String password
            , boolean isEnabled, boolean isAccountNonExpired, boolean isAccountNonLocked
            , boolean isCredentialsNonExpired, Collection<? extends GrantedAuthority> authorities, boolean firstLogin) {
        this.oAuth2UserInfo = attributes;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.authorities = authorities;
        this.firstLogin = firstLogin;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfo.getAttributes();
    }

    @Override
    public String getName() {
        return oAuth2UserInfo.getProvider();
    }
}
