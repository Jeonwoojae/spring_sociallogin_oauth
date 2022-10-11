package com.example.spring_sociallogin_oauth.oauth2.service;

import com.example.spring_sociallogin_oauth.Provider;
import com.example.spring_sociallogin_oauth.oauth2.domain.Role;
import com.example.spring_sociallogin_oauth.oauth2.domain.User;
import com.example.spring_sociallogin_oauth.oauth2.domain.UserRepository;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly=true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public int delete(long id) {
        Optional<User> oUser = userRepository.findById(id);
        if(oUser.isPresent()) {
            userRepository.delete(oUser.get());
            return 1;
        }
        return 0;
    }

    @Transactional
    public int patch(long id, User updateUser) {
        //setter사용하면 안좋을거 같은데 방법 찾기
        Optional<User> oUser = userRepository.findById(id);
        if(oUser.isPresent()) {
            User user = oUser.get();
            if(StringUtils.isNotBlank(updateUser.getEmail()))
                user.setEmail(updateUser.getEmail());
            if(StringUtils.isNotBlank(updateUser.getName()))
                user.setName(updateUser.getName());
            if(StringUtils.isNotBlank(updateUser.getPicture()))
                user.setPicture(updateUser.getName());
            if(updateUser.getRole() != null)
                user.setRole(updateUser.getRole());
            if(updateUser.getProvider() != null)
                user.setProvider(updateUser.getProvider());
            userRepository.save(user);
            return 1;
        }
        return 0;
    }

    @Transactional//메서드 일련 과정이 오류 없으면 commit()호출
    public User save(User inputUser) {
        User user = User.builder()
                .name(inputUser.getName())
                .email(inputUser.getEmail())
                .picture(inputUser.getPicture())
                .provider(inputUser.getProvider())
                .role(inputUser.getRole())
                .build();
        return userRepository.save(user);
    }
}
