package nk.service.NStory.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;

@RequiredArgsConstructor
@Getter @ToString
public class CustomUserDetails implements UserDetails {
    @Serial
    private static final long serialVersionUID = -6613434008874770290L;

    private String username;
    private String email;
    private String password;
    private boolean isEnabled; // 계정 활성화 여부
    private boolean isAccountNonExpired; //계정 만료 여부
    private boolean isAccountNonLocked; //계정 잠김 여부
    private boolean isCredentialsNonExpired; // 계정 비밀번호 만료 여부
    private Collection<? extends GrantedAuthority> authorities;// 권한 목록
    private boolean firstLogin;

    //Login
    public CustomUserDetails(String username, String email, String password
            , boolean isEnabled, boolean isAccountNonExpired, boolean isAccountNonLocked
            , boolean isCredentialsNonExpired, Collection<? extends GrantedAuthority> authorities, boolean firstLogin) {
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
}
