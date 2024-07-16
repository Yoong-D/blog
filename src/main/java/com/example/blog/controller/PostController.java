package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    // 게시글 작성 폼
    @GetMapping("/postform")
    public String postForm(){
        return "post";
    }

    // 게시글 상세보기
    @GetMapping("/@{username}/{title}")
    public String postView(@PathVariable("username") String username, @PathVariable("title")String title, Model model){
        log.info(username +"의 \" " +  title +" \" 게시물 상세 보기");
        String PostTitle = title.replace("-", " ");
        Post post =  postService.findByUsernameAndTitle(username,PostTitle);
        model.addAttribute("post", post);

        return "postView";
    }

    // 게시글 수정하기
    @GetMapping("/write")
    public String postModify(@RequestParam(value="id")Long id, Model model){
        Post post = postService.findById(id).get();
        model.addAttribute("post",post);
        return "postModify";
    }

    // 게시글 삭제하기
}
