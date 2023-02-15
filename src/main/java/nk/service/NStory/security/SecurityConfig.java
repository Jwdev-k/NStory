package nk.service.NStory.security;

import nk.service.NStory.security.handler.FailureHandler;
import nk.service.NStory.security.handler.SuccessHandler;
import nk.service.NStory.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;

    @Bean //정적 파일 ignoring
    public WebSecurityCustomizer customizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean //웹 필터
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(accountService);
        authenticationManager = authenticationManagerBuilder.build();

        http.csrf().disable()
                .authorizeHttpRequests().anyRequest().permitAll()
                .and().httpBasic(Customizer.withDefaults())
                .authenticationManager(authenticationManager).anonymous().disable();
        http.formLogin()
                .loginPage("/login").usernameParameter("email").passwordParameter("password")
                .loginProcessingUrl("/perform_login").successHandler(new SuccessHandler())
                .failureHandler(new FailureHandler());
        /*                .oauth2Login()
                .loginPage("/login").permitAll()
                .failureUrl("/login").permitAll()
                .successHandler(new SuccessHandler())
                .userInfoEndpoint()
                .userService(); // 소셜 로그인을 위한 클래스 설정*/

        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .clearAuthentication(true).logoutSuccessUrl("/login").invalidateHttpSession(true).deleteCookies("JSESSIONID");

        http.sessionManagement()
                .invalidSessionUrl("/login").maximumSessions(1).maxSessionsPreventsLogin(true).expiredUrl("/login")
                .sessionRegistry(sessionRegistry());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
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
