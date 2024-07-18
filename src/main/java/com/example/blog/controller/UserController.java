package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.repository.PostRepository;
import com.example.blog.service.PostService;
import com.example.blog.service.accountService.RefreshTokenService;
import com.example.blog.service.accountService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final PostService postService;
    private final PostRepository postRepository;

    // 메인 화면 - 페이징 처리
    @GetMapping("/")
    public String LoginMain(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            Model model) {

        // 페이지 번호는 0부터 시작하므로 1을 뺍니다.
        Page<Post> posts = postService.pagingPost(page - 1, size);
        model.addAttribute("posts", posts);
        model.addAttribute("url","/");

        return "home";
    }

    // 사용자 상세 페이지 -> http://도메인/@{useranme}
    @GetMapping("/@{username}")
    public String blog(@RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       @PathVariable("username") String username, Model model) {
        // 페이지 번호는 0부터 시작하므로 1을 뺍니다.
        Page<Post> posts = postService.recentPagingMyPost(page - 1, size,username);

        model.addAttribute("posts", posts);
        model.addAttribute("url", "/@" + username); // URL 수정
        return "home";
    }

    // 사용자 설정 페이지
    @GetMapping("/@{username}/settings")
    public String settings(){
        return "settings";    }

}
