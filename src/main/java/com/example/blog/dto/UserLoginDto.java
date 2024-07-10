package com.example.blog.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 클라이언트가 제출한 로그인 데이터
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDto {
    @NotEmpty
    private String username; // 아이디
    @NotEmpty
    //최소 하나 이상의 알파벳 문자(`a-z`, `A-Z`)를 포함할 것.
    //최소 하나 이상의 숫자(`0-9`)를 포함할 것.
    //최소 하나 이상의 특수 문자(`\W`, 예: `@`, `#`, `!`,`~` 등)를 포함할 것.
    //길이가 8자에서 20자 사이일 것.
    @Pattern(regexp=  "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")
    private String password; // 패스워드
}
