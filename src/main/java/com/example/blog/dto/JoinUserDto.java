package com.example.blog.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// 유저가 회원가입한 폼의 제출한 값을 담는 DTO
public class JoinUserDto {
    @NotEmpty
    private String username; // id
    //최소 하나 이상의 알파벳 문자(`a-z`, `A-Z`)를 포함할 것.
    //최소 하나 이상의 숫자(`0-9`)를 포함할 것.
    //최소 하나 이상의 특수 문자(`\W`, 예: `@`, `#`, `!`,`~` 등)를 포함할 것.
    //길이가 8자에서 20자 사이일 것.
    @NotEmpty
    @Pattern(regexp="^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")
    private String password; // password
    @NotEmpty
    private String againPassword; // password 확인
    @NotEmpty
    private String name; // 사용자 명
    @NotEmpty
    private String email; // 이름
}
