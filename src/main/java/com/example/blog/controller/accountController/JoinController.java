
package com.example.blog.controller.accountController;

import com.example.blog.domain.User;
import com.example.blog.service.accountService.UserService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@Slf4j
// 회원 가입 관련 컨트롤러
public class JoinController {
    private final UserService userService;

    // 회원 가입 폼
    @GetMapping("/signup")
    public String addUser(User user, Model model) {
        return "join";
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
        return "error2";
    }

}
