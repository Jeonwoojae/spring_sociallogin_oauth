package com.example.spring_sociallogin_oauth;

import com.example.spring_sociallogin_oauth.oauth2.domain.Role;
import com.example.spring_sociallogin_oauth.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.example.spring_sociallogin_oauth.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.example.spring_sociallogin_oauth.oauth2.service.CustomOAuth2AuthService;
import com.example.spring_sociallogin_oauth.oauth2.service.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SpringSecurityConfig {

    private final CustomOAuth2AuthService customOAuth2AuthService;

    private final CustomOidcUserService customOidcUserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .cors()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .oauth2Login()
                .userInfoEndpoint()
                //oauth2Login의 userInfoEndpoint가 openId일때는 Oidc
                // 그 이외의 정확한 scope가 있으면 OAuth
                .oidcUserService(customOidcUserService)
                .userService(customOAuth2AuthService)
                .and()
                //loadeUser가 성공하면 success 실패하면 failure
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)

                .and()
                .authorizeRequests()
                .antMatchers("/","/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
//                .antMatchers("/api/v1/**").hasRole((Role.USER.getTitle()))
                .anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**"));
    }
}
