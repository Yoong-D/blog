package com.example.blog.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="comment")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    // 댓글 작성자 id
    @Column(name="username", length = 50, nullable = false)
    private String username;

    //  댓글 작성자 명
    @Column(name="name", length = 50, nullable = false)
    private String name;

    // 댓글 내용
    @Column(name="comments", columnDefinition = "TEXT", nullable = false)
    private String comments;

    // 작성일
    @Column(name="created_at")
    private LocalDateTime created = LocalDateTime.now();

    // 수정일
    @Column(name="modify_at")
    private LocalDateTime modify = LocalDateTime.now();

    // 게시글 id - 외래키
    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    private Post post;


}
