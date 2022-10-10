package com.example.spring_sociallogin_oauth.view;

import com.example.spring_sociallogin_oauth.oauth2.dto.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class ViewController {
    private final HttpSession httpSession;

    public ViewController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    //Model 객체에 provider와 oauhId를 담아서 준다.
    @GetMapping("/social")
    public String socialSuccess(Model model,
                                @RequestParam(value = "provider", required = false) String provider,
                                @RequestParam(value = "oauthId", required = false) String oauthId
    ) {
        model.addAttribute("provider", provider);
        model.addAttribute("oauthId", oauthId);
        return "social-success";
    }

    @GetMapping("/api/v1/home")
    @ResponseBody
    public SessionUser Home(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        return user;
    }
    //권한별 보호 설정 https://code-boki.tistory.com/42?category=934141
}