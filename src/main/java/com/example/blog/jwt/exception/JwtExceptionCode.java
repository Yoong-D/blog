package com.example.blog.jwt.exception;

import lombok.Getter;

//JWT 인증 과정에서 발생할 수 있는 다양한 예외 상황을 정의
public enum JwtExceptionCode {

    // 알 수 없는 에러
    UNKNOWN_ERROR("UNKNOWN_ERROR", "UNKNOWN_ERROR"),
    // Headers에 토큰 형식의 값을 찾을 수 없음
    NOT_FOUND_TOKEN("NOT_FOUND_TOKEN", "Headers에 토큰 형식의 값 찾을 수 없음"),
    // 유효하지 않은 토큰
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 토큰"),
    // 기간이 만료된 토큰
    EXPIRED_TOKEN("EXPIRED_TOKEN", "기간이 만료된 토큰"),
    // 지원하지 않는 토큰
    UNSUPPORTED_TOKEN("UNSUPPORTED_TOKEN", "지원하지 않는 토큰");

    // 에러 코드
    @Getter
    private String code;

    // 에러 메시지
    @Getter
    private String message;

    // 생성자: 에러 코드와 메시지를 설정
    JwtExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}