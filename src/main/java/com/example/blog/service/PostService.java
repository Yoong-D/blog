package com.example.blog.service;

import com.example.blog.domain.Comment;
import com.example.blog.domain.Post;
import com.example.blog.domain.User;
import com.example.blog.dto.CommentDto;
import com.example.blog.dto.PostDto;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.service.accountService.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    // 해당 사용자가 가진 게시글 조회
    @Transactional
    public Post findByUsername(String username) {
        return postRepository.findByUsername(username);
    }

    // 포스트 id로 게시글 조회
    @Transactional
    public Optional<Post> findById(Long id){
        return postRepository.findById(id);
    }

    // 사용자(id)와, 제목으로 게시글 조회
    @Transactional
    public Post findByUsernameAndTitle(String username, String title){
        return postRepository.findByUsernameAndTitle(username,title);
    }

    // 페이징 처리(과거)
    @Transactional
    public Page<Post>  pagingPost(int page, int size){
        Pageable sortedByASCDate = PageRequest.of(page,size
                , Sort.by(Sort.Direction.ASC,"created"));
        Page<Post> posts = postRepository.findAll(sortedByASCDate);
        return posts;
    }

    // 페이징 처리(최신)
    @Transactional
    public Page<Post>  recentPagingPost(int page, int size){
        Pageable sortedByDESCDate = PageRequest.of(page,size
                , Sort.by(Sort.Direction.DESC,"created"));
        Page<Post> posts = postRepository.findAll(sortedByDESCDate);
        return posts;
    }
    // 나의 글 페이징 처리(과거)
    @Transactional
    public Page<Post> recentPagingMyPost(int page, int size, String username) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "created"));
        return postRepository.findByUsername(username, pageable);
    }


    // 나의 글 페이징 처리(최신)pagingMyPost
    @Transactional
    public Page<Post> pagingMyPost(int page, int size, String username) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"));
        return postRepository.findByUsername(username, pageable);
    }
    // 게시글 저장
    @Transactional
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

    // 게시글 수정
    @Transactional
    public void updatePost(Long id, Post post){
        log.info("post 수정 저장");
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post OriginalPost = optionalPost.get();

            OriginalPost.setTitle(post.getTitle());
            OriginalPost.setTag(post.getTag());
            String contents = post.getContents().replaceAll("\\<.*?\\>", "");
            OriginalPost.setContents(contents);
            OriginalPost.setSeries(post.getSeries());
            OriginalPost.setCreated(OriginalPost.getCreated());
            OriginalPost.setModify(LocalDateTime.now());
            postRepository.save(OriginalPost);
        } else {
            throw new EntityNotFoundException("Post not found with id: " + id);
        }
    }

    // 게시글 삭제
    @Transactional
    public void deleteById(Long id){
        postRepository.deleteById(id);
    }

    // 검색 게시글 찾기
    public List<Post> searchPosts(String search) {
        return postRepository.findBySearch(search);
    }

}