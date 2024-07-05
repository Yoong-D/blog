package com.example.blog.controller;

import com.example.blog.domain.User;
import com.example.blog.service.UserService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // [ 로그인 ]
    // 메인 화면(로그인X)
    @GetMapping("/")
    public String LoginMain() {
        return "index";
    }

    // 로그인 폼 - 구현 완료
    @GetMapping("/loginform")
    public String loginform() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model) {
        User dbUser = userService.findByUsername(user.getUsername());
        if (passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            return "redirect:/welcome";
        }
        model.addAttribute("error_message", "로그인에 실패하였습니다.");
        return "login";
    }

    // 사용자 상세 페이지 -> http://도메인/@{useranme}
    @GetMapping("/@{username}")
    public String blog() {
        return "mypage";
    }


    // [ 회원 가입 ]
    // 회원 가입 폼
    @GetMapping("/signup")
    public String addUser(User user, Model model) {
        return "signup";
    }

    // 회원 가입 정보를 받아 들여 회원 등록을 수행하고, 성공시 환영 메시지 보여주는 화면으로 리다이렉트
    @PostMapping("/userreg")
    public String userReg(@ModelAttribute("user") User user,
                          @RequestParam("error_message") String error_message,
                          BindingResult result,
                          Model model) {

        if (error_message.equals("none") && !result.hasErrors()) { // 에러 메시지가 없으면 회원 가입 완료
            userService.addUser(user);
            return "redirect:/welcome";
        } else {
            model.addAttribute("error_message", error_message); // 에러 메시지가 있다면 error 페이지로 이동
            return "/error";
        }


    }

    // 회원 가입이 성공적으로 완료된 후, 환영 메시지를 포함한 화면을 보여준다.
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    // 에러페이지
    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }

    // [ 권한 ]
    @GetMapping("access-denied")
    public String access_denied() {
        return "access-denied";
    }

}
