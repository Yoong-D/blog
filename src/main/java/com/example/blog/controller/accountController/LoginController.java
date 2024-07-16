package com.example.blog.controller.accountController;

import com.example.blog.domain.Post;
import com.example.blog.service.PostService;
import com.example.blog.service.accountService.RefreshTokenService;
import com.example.blog.service.accountService.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
// 로그인 관련 컨트롤러
public class LoginController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final PostService postService;

    // 메인 화면(로그인X)
    @GetMapping("/")
    public String LoginMain(Model model) {
        List<Post> posts = postService.allPost();
        model.addAttribute("posts", posts);
        return "home";
    }

    // 로그인 폼 - 구현 완료
    @GetMapping("/loginform")
    public String loginform() {
        return "login";
    }

    // 사용자 상세 페이지 -> http://도메인/@{useranme}
    @GetMapping("/@{username}")
    public String blog(@PathVariable("username") String useranme) {
        return "mypage";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        log.info("로그아웃");
        // 1. DB에 해당 Refresh Token 삭제
        String refreshToke = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refreshToken")){
                refreshToke = cookie.getValue();
                break;
            }
        }
        refreshTokenService.deleteRefreshToken(refreshToke);

        // 2. 쿠키 삭제
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

        return "redirect:/";
    }


}