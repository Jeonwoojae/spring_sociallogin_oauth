package com.example.spring_sociallogin_oauth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringSocialloginOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSocialloginOauthApplication.class, args);
    }

}
