package com.example.blog.repository;

import com.example.blog.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, PagingAndSortingRepository<Post,Long> {

    // 게시글 조회
    Post findByUsername(String username);

    // 사용자(id)와, 제목으로 게시글 조회
    Post findByUsernameAndTitle(String username, String title);

    // 내 글 페이징 처리
    Page<Post> findByUsername(String username, Pageable pageable);

    // 내글 검색 처리
    @Query("SELECT p FROM Post p " +
            "JOIN p.author u " +
            "WHERE p.title LIKE %:search% " +
            "OR p.contents LIKE %:search% " +
            "OR u.username LIKE %:search%")
    List<Post> findBySearch(@Param("search") String search);

}
