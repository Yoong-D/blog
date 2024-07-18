package com.example.blog.domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id; // 게시글 id

    @Column(name = "username", nullable = false, length = 50)
    private String username; // 작성자 id - 외래키

    @Column(name = "name", nullable = false, length = 50)
    private String name; // 작성자 명

    @Column(name = "title", nullable = false, length = 100)
    private String title; // 글 제목

    @Column(name = "contents", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String contents; // 글 내용

    @Column(name = "tag", columnDefinition = "TEXT")
    private String tag;// 태그 목록(핵심키워드)

    @Column(name = "series", length = 100)
    private String series; // 시리즈명(주제별 묶음(?))

    @Column(name = "like_count", columnDefinition = "INT DEFAULT 0")
    private int likeCount = 0; // 좋아요 개수

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime created = LocalDateTime.now(); // 최초 등록일

    @Column(name="modify_at", nullable = false)
    private LocalDateTime modify; // 최근 수정일

    // 1이 공개, 0이 비공개
    @Column(name = "access", columnDefinition = "BOOLEAN DEFAULT 1")
    private boolean access = true; // 글 비공개 공개 여부

    // 유저와 다대일 관계
    // name = "username": 외래 키 컬럼의 이름을 지정
    // nullable = false: 외래 키 컬럼이 null을 허용하지 않도록 설정
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author; // 작성자

    // 대글과 일대 다 관계
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

}
