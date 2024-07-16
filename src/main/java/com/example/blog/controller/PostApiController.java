package com.example.blog.controller;

import com.example.blog.domain.User;
import com.example.blog.dto.PostDto;
import com.example.blog.service.PostService;
import com.example.blog.service.accountService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostApiController {
    private final PostService postService;
    private final UserService userService;


    @PostMapping("/api/postform")
    public ResponseEntity postForm(@RequestBody PostDto postDto, HttpServletRequest request){
        log.info("post api 실행");
        // 토큰 추출하여 해당 사용자 조회
        User user = userService.findByAccessToken(request);
        if(postDto == null){
            log.info("PostDto 객체가 서버로 안넘어옴");
            return new ResponseEntity("PostDto 객체가 서버로 안넘어옴", HttpStatus.BAD_REQUEST);
        }
        if(user ==  null){
            log.info("사용자 조회 실패");
            return new ResponseEntity("사용자 조회 실패", HttpStatus.BAD_REQUEST);
        }
        // DB에 게시글 저장
        postService.savePost(user,postDto);
        return new ResponseEntity(postDto, HttpStatus.OK);
    }
}
