package com.example.blog.repository;

import com.example.blog.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, PagingAndSortingRepository<Post,Long> {

    // 게시글 조회
    Post findByUsername(String username);

    // 사용자(id)와, 제목으로 게시글 조회
    Post findByUsernameAndTitle(String username, String title);

    List<Post> findAllByOrderByCreatedDesc();

    // 페이징 처리를 위한 메서드
    Page<Post> findAllByOrderByCreatedDesc(Pageable pageable);

}
