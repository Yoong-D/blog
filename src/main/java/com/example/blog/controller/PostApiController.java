package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.domain.User;
import com.example.blog.dto.CommentDto;
import com.example.blog.dto.PostDto;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import com.example.blog.service.accountService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostApiController {
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    // 게시글 업로드
    @PostMapping("/api/postform")
    public ResponseEntity postForm(@RequestBody PostDto postDto, HttpServletRequest request){
        log.info("post api 실행");
        // 토큰 추출하여 해당 사용자 조회
        User user = userService.findByAccessToken(request);
        if(postDto == null){
            log.info("PostDto 객체가 서버로 안넘어옴");
            return new ResponseEntity("PostDto 객체가 서버로 안넘어옴", HttpStatus.BAD_REQUEST);
        }
        if(postDto.getTitle().equals("")){
            log.info("제목이 없음");
            return new ResponseEntity("제목을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        if(postDto.getContent().equals("")){
            log.info("내용이 비어있음.");
            return new ResponseEntity("내용을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        if(user ==  null){
            log.info("사용자 조회 실패");
            return new ResponseEntity("사용자 조회 실패", HttpStatus.BAD_REQUEST);
        }
        // DB에 게시글 저장
        postService.savePost(user,postDto);
        return new ResponseEntity(postDto, HttpStatus.OK);
    }

    // 이미지 업로드
    @PostMapping("/api/image")
    public void imageUpload(){

    }

    // 댓글
    @PostMapping("/api/comment")
    public ResponseEntity comment(@RequestBody CommentDto commentDto){
        log.info("/api/comment Post 요청");
        Post post = postService.findByUsernameAndTitle(commentDto.getUsername(), commentDto.getPostTitle());
        if(commentService.addComment(post, commentDto) != null){
            return new ResponseEntity(commentDto,HttpStatus.OK);
        }else{
            return new ResponseEntity("댓글 등록에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }


}
