package com.example.spring_sociallogin_oauth.oauth2.handler;

import com.example.spring_sociallogin_oauth.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("성공");
        //성공하면 /social경로로 값들 보낸다.
        String[] path = request.getRequestURI().split("/");
        Provider provider = Provider.valueOf(path[path.length - 1].toUpperCase());
        String oauthId = authentication.getName();
        String uri = UriComponentsBuilder.fromUriString("http://localhost:8080/social")
                .queryParam("provider",provider)
                .queryParam("oauthId", oauthId)
                .build().toUriString();
        response.sendRedirect(uri);
        //여기에 JwtUtil과 JwtTokenProvider로 토큰화 할 예정
        //?token=JWT-TOKEN
    }
}
