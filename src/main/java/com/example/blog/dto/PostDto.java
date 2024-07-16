package com.example.blog.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// 클라이언트가 제출한 게시글 데이터
public class PostDto {

    @NotEmpty
    private String title; // 글 제목
    @NotEmpty
    private String content; // 글 내용
    private String tag; // 글의 태그(키워드)
    private String series; // 시리즈(부제?)

}


