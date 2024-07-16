package com.example.blog.service;

import com.example.blog.domain.Post;
import com.example.blog.domain.User;
import com.example.blog.dto.PostDto;
import com.example.blog.repository.PostRepository;
import com.example.blog.service.accountService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    // 해당 사용자가 가진 게시글 조회
    public Post findByUsername(String username) {
        return postRepository.findByUsername(username);
    }

    // 포스트 id로 게시글 조회
    public Optional<Post> findById(Long id){
        return postRepository.findById(id);
    }

    // 사용자(id)와, 제목으로 게시글 조회
    public Post findByUsernameAndTitle(String username, String title){
        return postRepository.findByUsernameAndTitle(username,title);
    }
    // 게시글 저장
    public void savePost(User user, PostDto postDto) {
        log.info("post 저장");
        Post post = new Post();
        // 에디터로 받아온 post 내용 안에 태그도 존재 -> 정규식을 활용하여 태그 없에기
        log.info("정규식 활용 전 " + postDto.getContent());
        String contents = postDto.getContent().replaceAll("\\<.*?\\>", "");
        log.info("정규식 활용 후 " + contents);

        post.setContents(contents);
        post.setTitle(postDto.getTitle());
        post.setTag(postDto.getTag());
        post.setSeries(post.getSeries());
        post.setName(user.getName());
        post.setUsername(user.getUsername());
        post.setAuthor(user);
        post.setModify(LocalDateTime.now());
        postRepository.save(post);
        // 게시글 삭제
    }

    // 전체 게시글 반환
    public List<Post> allPost(){
        return postRepository.findAll();
    }

}