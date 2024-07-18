package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.dto.PostDto;
import com.example.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 게시글 수정 폼
    @GetMapping("/write")
    public String postModify(@RequestParam(value="id")Long id, Model model){
        log.info("수정 폼 id : "+ id);
        Post post = postService.findById(id).get();
        model.addAttribute("post",post);
        return "postModify";
    }
    // 게시글 수정
    @PostMapping("/modify")
    public String postModfiy(@RequestParam("id")Long id, @ModelAttribute Post post){
        log.info("수정 할게요~~ id : "+ id);
        postService.updatePost(id,post);
        return "redirect:/";
    }

    // 게시글 삭제하기
    @GetMapping("/delete/{username}/{title}")
    public String postDelete(@PathVariable("username") String username, @PathVariable("title") String title){
        log.info("삭제할 포스트 -> 사용자 : " + username + " 제목 : " + title);
        Post post = postService.findByUsernameAndTitle(username,title);
        postService.deleteById(post.getId());
        return "redirect:/";
    }

    // 네이비게이션 바 - 최신글 순 -> 페이징 처리  recentPagingPost
    @GetMapping("/recent")
    public String LoginMain(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,Model model) {

        // 페이지 번호는 0부터 시작하므로 1을 뺍니다.
        Page<Post> posts = postService.recentPagingPost(page - 1, size);
        model.addAttribute("posts", posts);
        model.addAttribute("url","/recent");
        return "home";
    }


}
