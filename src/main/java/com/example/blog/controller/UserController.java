package com.example.blog.controller;

import com.example.blog.domain.User;
import com.example.blog.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

// [ 로그인 ]
    // 메인 화면(로그인X)
    @GetMapping("/")
    public String LoginMain(){
        if(userService.getLink()){ // 서비스 링크 변수를 통해 true면 로그인중, false면 로그아웃중으로 판단
            return "afterlogin";
        }
        return "index";
    }

    // 로그인 폼 - 구현 완료
    @GetMapping("/loginform")
    public String loginform(){
        return "login";
    }

    // 로그인 처리 + 쿠기 생성
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpServletResponse response){
        User member = userService.findByUsername(username);
        if(member != null && member.getPassword().equals(password)){ // 패스워드가 일치한다면
            Cookie cookie = new Cookie("user",username); // user라는 쿠키에 아이디 넣기
            cookie.setPath("/"); // 모든 경로에서 접근 가능하게 설정
            cookie.setHttpOnly(true); // 자바스크립트로 쿠키에 접근 할수 없게 막기
            response.addCookie(cookie); // 응답으로 브라우저에 쿠키값 넘기기
            return "redirect:/@" + username ; // 로그인 성공시 사용자 상세 페이지로 리다이렉트

        }else{
            model.addAttribute("error_message", "아이디 또는 비밀번호를 다시 입력하세요.");
            return "/login";
        }
    }
    // 사용자 상세 페이지 -> http://도메인/@{useranme}
    // 사용자 인증 쿠키를 검증하여 인증된 사용자만 접근 가능 -> 구현해야한다.
    @GetMapping("/@{username}")
    public String blog(@PathVariable("username") String useranme, Model model){
        model.addAttribute("username");
        return "blog";
    }

    // 같은 이름의 쿠키가 2개 이상 있을수 없다는 특성을 사용하여 로그아웃 구현
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("user", "");
        cookie.setPath("/");
        cookie.setMaxAge(0); // 유지 기간 0초
        response.addCookie(cookie);
        return "redirect:/";
    }
// [ 회원 가입 ]
    // 회원 가입 폼
    @GetMapping("/signup")
    public String addUser(User user, Model model){
        return "signup";
    }

    // 회원 가입 정보를 받아 들여 회원 등록을 수행하고, 성공시 환영 메시지 보여주는 화면으로 리다이렉트
    @PostMapping("/userreg")
    public String userReg(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("name") String name,
                          @RequestParam("email")String email,
                          @RequestParam("error_message") String error_message,
                          Model model){

            if(error_message.equals("none")){ // 에러 메시지가 없으면 회원 가입 완료
                return "redirect:/welcome";
            }else{
                model.addAttribute("error_message", error_message); // 에러 메시지가 있다면 error 페이지로 이동
                return "/error";
            }


    }

    // 회원 가입이 성공적으로 완료된 후, 환영 메시지를 포함한 화면을 보여준다.
    @GetMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    // 에러페이지
    @GetMapping("/error")
    public String error(Model model){
        return "error";
    }

// [ 권한 ]
    @GetMapping("access-denied")
    public String access_denied(){
        return "access-denied";
    }

}
