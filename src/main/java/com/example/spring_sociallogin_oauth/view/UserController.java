package com.example.spring_sociallogin_oauth.view;

import com.example.spring_sociallogin_oauth.Provider;
import com.example.spring_sociallogin_oauth.oauth2.domain.Role;
import com.example.spring_sociallogin_oauth.oauth2.domain.User;
import com.example.spring_sociallogin_oauth.oauth2.domain.UserRepository;
import com.example.spring_sociallogin_oauth.oauth2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping(value = "/users",produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController //뷰 페이지를 이용하지 않고 json같은 데이터로 응답 받고자 할 때
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable("id") long id) {
        Map<String, Object> response = new HashMap<>();
        if(userService.delete(id) > 0) {
            response.put("result","SUCCESS");
        } else {
            response.put("result","FAIL");
            response.put("reason","일치하는 회원정보가 없습니다.");
        }
        return response;
    }

    @PatchMapping("/{id}")
    public Map<String, Object> patch(@PathVariable("id") long id, @RequestBody User updateUser){
        Map<String, Object> response = new HashMap<>();
        if(userService.patch(id, updateUser)>0) {
            response.put("result","SUCCESS");
        } else {
            response.put("result", "FAIL");
            response.put("reason","일치하는 회원 정보가 없습니다.");
        }
        return response;
    }

    @PostMapping("")
    public Map<String, Object> save(@RequestBody User newUser) {
        Map<String, Object>response = new HashMap<>();
        User user = userService.save(newUser);
        if(user != null) {
            response.put("result","SUCCESS");
            response.put("user",user);
        } else {
            response.put("result", "FAIL");
            response.put("reason", "회원 가입 실패");
        }

        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable("id") User user) {
        Map<String, Object> response = new HashMap<>();

//        Optional<User> oUser = userService.findById(id);
        if(user != null) {
            response.put("result","SUCCESS");
            response.put("user",user);
        }else {
            response.put("result", "FAIL");
            response.put("reason", "일치하는 회원 정보가 없습니다.");
        }
        return response;
    }
}
