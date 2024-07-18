package com.example.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String postTitle; // 게시글 제목
    private String username; //  게시글 작성자 id
    private String writer; // 댓글 작성자 id
    private String name; // 작성자 명
    private String comments; // 내용
}
