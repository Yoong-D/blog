package com.example.blog.controller;

import com.example.blog.domain.User;
import com.example.blog.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    // 메인 화면(로그인X)
    @GetMapping("/")
    public String LoginMain(){
        if(userService.getLink()){ // 서비스 링크 변수를 통해 true면 로그인중, false면 로그아웃중으로 판단
            return "afterlogin";
        }
        return "index";
    }

    // 로그인 폼
    @GetMapping("/loginform")
    public String loginform(){
        return "login";
    }

    // 로그인 폼 처리
    @PostMapping("/login")
    public String login(@RequestParam(name = "username") String name, @RequestParam(name = "password") String password, Model model, HttpServletResponse response){
        if(userService.user(name,password) ){
            // 로그인 성공 시 사용자를 담을 쿠키 생성 - 사용자 id 담음
            Cookie cookie = new Cookie("user", name);
            cookie.setPath("/");
            response.addCookie(cookie);
            userService.setLink(true);
            return "redirect:/@" + name ;
        }else{
            model.addAttribute("error", "로그인에 실패하였습니다.");
            return "/error";
        }
    }
    @PostMapping("/logout/{username}")
    public String logout(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (username.equals(cookie.getValue())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/"); // 쿠키가 설정된 경로를 명확히 지정
                    response.addCookie(cookie);
                    userService.setLink(false);
                    break; // 찾은 쿠키를 삭제하고 루프 종료
                }
            }
        }
        return "redirect:/";
    }
    // 로그인 후 사용자 페이지 -> http://도메인/@{useranme}
    @GetMapping("/@{username}")
    public String userPage(@PathVariable String username, Model model, HttpServletRequest request ){
        if(!userService.userPage(username, request)){
            model.addAttribute("error","당신은 이 페이지에 접근할 권한이 없습니다.");
            return "error";
        }
        model.addAttribute("username", username);
        return "blog";
    }

    // 회원 가입 폼
    @GetMapping("/adduser")
    public String addUser(User user, Model model){
        return "adduser";
    }

    // 회원 가입 정보를 받아 들여 회원 등록을 수행하고, 성공시 환영 메시지 보여주는 화면으로 리다이렉트
    @PostMapping("/userreg")
    public String userReg(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("name") String name,
                          @RequestParam("email")String email,
                          @RequestParam(name = "errormessage",required = false) String errorMessage,
                          Model model){
        if(errorMessage != null &&errorMessage.equals("true")){
            userService.addUser(new User(username,password,name,email));
            return "redirect:/welcome";
        }else{
            model.addAttribute("error", errorMessage);
            return "error";
        }

    }

    // 회원 가입이 성공적으로 완료된 후, 환영 메시지를 포함한 화면을 보여준다.
    @GetMapping("/welcome")
    public String welcome(){
        return "welcome";
    }


}
