package nk.service.NStory.security;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.security.handler.FailureHandler;
import nk.service.NStory.security.handler.SuccessHandler;
import nk.service.NStory.security.handler.customLogoutSuccessHandler;
import nk.service.NStory.service.impl.AccountService;
import nk.service.NStory.service.impl.OAuth2LoginService;
import nk.service.NStory.service.impl.UserLoginService;
import nk.service.NStory.utils.UpdateStatus;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    AuthenticationManager authenticationManager;
    private final UserLoginService userLoginService;
    private final OAuth2LoginService oAuth2LoginService;
    private final AccountService accountService;
    private final UpdateStatus updateStatus;

    @Bean //정적 파일 ignoring
    public WebSecurityCustomizer customizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean //웹 필터
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //deprecated에 따른 필터 설정 기존 메서드식에서 람다식으로 변경.
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests.anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                .anonymous(AbstractHttpConfigurer::disable)

                .formLogin((formLogin) ->
                        formLogin.loginPage("/login").usernameParameter("email").passwordParameter("password")
                                .loginProcessingUrl("/perform_login")
                                .successHandler(new SuccessHandler(customRememberMeServices()))
                                .failureHandler(new FailureHandler()))

                .oauth2Login((oauth2Login) ->
                        oauth2Login.loginPage("/login")
                                .failureUrl("/login")
                                .successHandler(new SuccessHandler(customRememberMeServices()))
                                .userInfoEndpoint((userInfoEndpointConfig) ->
                                        userInfoEndpointConfig.userService(oAuth2LoginService))) // 소셜 로그인을 위한 클래스 설정*/)
                .rememberMe((rememberMe) ->
                        rememberMe.rememberMeServices(customRememberMeServices())
                                .authenticationSuccessHandler(new SuccessHandler(customRememberMeServices()))) // 커스텀 자동로그인 설정

                .logout((logout) ->
                        logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessHandler(new customLogoutSuccessHandler())
                                .clearAuthentication(true).invalidateHttpSession(true)
                                .deleteCookies("6ASDF636ADVBN8J$KL","7adbbb4c6ATLG"))

                .sessionManagement((sessionMgt) ->
                        sessionMgt.sessionFixation().migrateSession()
                                .invalidSessionUrl("/login").maximumSessions(1)
                                .maxSessionsPreventsLogin(true).expiredUrl("/login")
                                .sessionRegistry(sessionRegistry()))
                .headers(headers ->
                        headers.xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)));

        return http.build();
    }

    @Bean
    public TokenBasedRememberMeServices customRememberMeServices() {
        CustomRememberMe customRememberMeServices = new CustomRememberMe("NStory_ATLG_customKey", userLoginService, accountService, updateStatus);
        customRememberMeServices.setTokenValiditySeconds(86400 * 30);
        customRememberMeServices.setParameter("remember-me");
        customRememberMeServices.setCookieName("7adbbb4c6ATLG");
        return customRememberMeServices;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
