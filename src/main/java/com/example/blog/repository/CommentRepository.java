package com.example.blog.repository;

import com.example.blog.domain.Comment;
import com.example.blog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Comment findByUsername(String username);
    List<Comment> findByPost(Post post);
}
