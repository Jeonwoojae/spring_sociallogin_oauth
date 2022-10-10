package com.example.spring_sociallogin_oauth.oauth2.dto;

import com.example.spring_sociallogin_oauth.Provider;
import com.example.spring_sociallogin_oauth.oauth2.domain.Role;
import com.example.spring_sociallogin_oauth.oauth2.domain.User;
import com.example.spring_sociallogin_oauth.oauth2.exception.OAuth2RegistrationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
public class OAuth2Attributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String oauthId;
    private final String nickname;
    private final String email;
    private final String picture;
    private final Provider provider;

    @Builder
    public OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String oauthId, String nickname, String email, String picture, Provider provider){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
    }

    @SneakyThrows
    public static OAuth2Attributes of(String registerationId, String userNameAttributeName, Map<String, Object> attributes){
        //json형태로 보고싶은 객체를 볼 수 있다.
        log.info("userNameAttributeName = {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userNameAttributeName));
        log.info("attributes = {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(attributes));
        //들어오는 provider마다 다른 메서드들 사용
        String registrationIdToLower = registerationId.toLowerCase();
        switch (registrationIdToLower) {
            case "naver": return ofNaver(userNameAttributeName, attributes);
            case "kakao": return ofKakao(userNameAttributeName, attributes);
            case "google": return ofGoogle(userNameAttributeName, attributes);
            default: throw new OAuth2RegistrationException("해당 소셜 로그인은 현재 지원하지 않습니다.");
        }
    }

    @SuppressWarnings("uncheked")
    private static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2Attributes.builder()
                .oauthId((String) response.get("id"))
                .nickname((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .provider(Provider.NAVER)
                .attributes(response)
                .nameAttributeKey("id")
                .build();
    }

    @SuppressWarnings("uncheked")
    private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        return OAuth2Attributes.builder()
                .oauthId(attributes.get(userNameAttributeName).toString())
                .nickname((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .provider(Provider.KAKAO)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .oauthId((String) attributes.get(userNameAttributeName))
                .nickname((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .provider(Provider.GOOGLE)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    //User를 만들어 JPA로 저장
    public User toEntity(){
        return User.builder()
                .name(nickname)
                .email(email)
                .picture(picture)
                .provider(provider)
                .role(Role.USER)
                .build();
    }
}
