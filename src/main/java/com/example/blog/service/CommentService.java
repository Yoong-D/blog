package com.example.blog.service;

import com.example.blog.domain.Comment;
import com.example.blog.domain.Post;
import com.example.blog.dto.CommentDto;
import com.example.blog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;


    // 댓글 등록
    @Transactional(rollbackFor = DataAccessException.class)
    public Comment addComment(Post post, CommentDto commentDto) {
        Comment comment = null; // 초기화된 결과 값
        log.info("사용자 아이디" + commentDto.getUsername());
        log.info("사용자 이름 " + commentDto.getName());
        try {
            comment = new Comment();
            comment.setUsername(commentDto.getUsername());
            comment.setName(commentDto.getName());
            comment.setComments(commentDto.getComments());
            comment.setCreated(LocalDateTime.now());
            comment.setModify(LocalDateTime.now());
            comment.setPost(post);
            commentRepository.save(comment);
            log.info("댓글 db 저장");

        } catch (DataIntegrityViolationException e) {
            log.info("댓글 db 저장 실패");
            System.out.println(e);
        }

        return comment; // 최종 결과 반환
    }

    // 게시물에 등록된 댓글 리스트
    @Transactional
    public  List<Comment> CommentList(Post post){
        return commentRepository.findByPost(post);
    }
}
