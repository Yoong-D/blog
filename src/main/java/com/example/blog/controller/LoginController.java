package com.example.blog.controller;

import com.example.blog.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@Slf4j
// 로그인 관련 컨트롤러
public class LoginController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // 메인 화면(로그인X)
    @GetMapping("/")
    public String LoginMain() {
        return "home";
    }

    // 로그인 폼 - 구현 완료
    @GetMapping("/loginform")
    public String loginform() {
        return "login";
    }

    // 사용자 상세 페이지 -> http://도메인/@{useranme}
    @GetMapping("/@{username}")
    public String blog(@PathVariable("username") String useranme, Model model) {
        log.info("실행은 되나요?");
        model.addAttribute("username");
        return "mypage";
    }

//    @GetMapping("/@{username}")
//    public String blog(@PathVariable("username") String useranme, Model model){
//        model.addAttribute("username");
//        return "blog";
//    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("accessToken",null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken",null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        return "redirect:/home";
    }


}
