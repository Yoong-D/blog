package com.example.blog.jwt.exception;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

// 시큐리티가 인증되지 않은 사용자가 인증이 필요한 리소스에 접근 할때 동작하게 하는 인터페이스
// 사용자가 로그인하지 않은 상태에서 보호된 페이지에 접근하려고 할 때
// 사용자의 세션이 만료되어 인증이 필요한 자원에 접근하지 못할 때
@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    // 사용자가 인증되지 않았을 시, 처리 구현
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 요청 속성에서 예외 메시지 가져오기
        String exception = (String) request.getAttribute("exception");
        // 예외가 존재할 때
        if(exception!=null){
            // 만약 사용자가 보낸 요청이 RERTful 요청이라면
            if (isRestRequest(request)) {
                handleRestResponse(request, response, exception); // 관련 핸들러 실행
            } else { // 페이지 요청이라면
                handlePageResponse(request, response, exception); // 관련 핸들러 실행
            }
        }
    }

    // 사용자 요청이 RESTful인지 판별
    private boolean isRestRequest(HttpServletRequest request) {
        // 요청 헤더에서 'X-Requested-With' 헤더 값을 가져옴
        String requestedWithHeader = request.getHeader("X-Requested-With");

        // 'X-Requested-With' 헤더 값이 'XMLHttpRequest'인지 확인
        // 또는 요청 URI가 '/api/'로 시작하는지 확인하여
        // 둘 중 하나라도 참이면 RESTful 요청으로 간주하고 true 반환
        return (requestedWithHeader != null && requestedWithHeader.equals("XMLHttpRequest")) || request.getRequestURI().startsWith("/api/");
    }

    // 사용자 요청이 페이지 요청라면 이에 대한 처리 구현
    private void handlePageResponse(HttpServletRequest request, HttpServletResponse response, String exception) throws IOException {
        // 로그에 에러 메시지 기록
        log.error("Page Request - Commence Get Exception : {}", exception);

        // 에러 존재할 시
        if (exception != null) {
            response.sendRedirect("/loginform"); // 로그인 폼으로 리다이렉트
        }
    }

    // 사용자 요청이 RESTful라면 이에 대한 처리 구현
    private void handleRestResponse(HttpServletRequest request, HttpServletResponse response, String exception) throws IOException {
        // 로그에 에러 메시지 기록
        log.error("Rest Request - Commence Get Exception : {}", exception);

        // 에러가 존재할 경우 종류에 따라서 응답 설정
        if (exception != null) {
            // 예외 코드가 INVALID_TOKEN인 경우: 유효하지 않은 JWT 토큰
            if (exception.equals(JwtExceptionCode.INVALID_TOKEN.getCode())) {
                log.error("entry point >> invalid token");
                setResponse(response, JwtExceptionCode.INVALID_TOKEN);
            }
            // 예외 코드가 EXPIRED_TOKEN인 경우: 만료된 JWT 토큰
            else if (exception.equals(JwtExceptionCode.EXPIRED_TOKEN.getCode())) {
                log.error("entry point >> expired token");
                setResponse(response, JwtExceptionCode.EXPIRED_TOKEN);
            }
            // 예외 코드가 UNSUPPORTED_TOKEN인 경우: 지원되지 않는 형식의 JWT 토큰
            else if (exception.equals(JwtExceptionCode.UNSUPPORTED_TOKEN.getCode())) {
                log.error("entry point >> unsupported token");
                setResponse(response, JwtExceptionCode.UNSUPPORTED_TOKEN);
            }
            // 예외 코드가 NOT_FOUND_TOKEN인 경우: JWT 토큰이 요청에 없음
            else if (exception.equals(JwtExceptionCode.NOT_FOUND_TOKEN.getCode())) {
                log.error("entry point >> not found token");
                setResponse(response, JwtExceptionCode.NOT_FOUND_TOKEN);
            }
            // 위의 조건에 해당하지 않는 경우: 알 수 없는 에러
            else {
                setResponse(response, JwtExceptionCode.UNKNOWN_ERROR);
            }
        }
        // 예외가 없는 경우: 알 수 없는 에러
        else {
            setResponse(response, JwtExceptionCode.UNKNOWN_ERROR);
        }
    }

    // JWT 인증 과정에서 발생한 예외에 대한 HTTP 응답 설정
    // 응답의 Content-Type과 상태 코드를 설정하고, 예외 메시지와 코드를 포함한 JSON 응답을 생성하여 클라이언트에게 반환
    private void setResponse(HttpServletResponse response, JwtExceptionCode exceptionCode) throws IOException {
        // 응답의 Content-Type을 JSON 형식으로 설정하고, 문자 인코딩을 UTF-8로 설정
        response.setContentType("application/json;charset=UTF-8");
        // 응답 상태 코드를 401 Unauthorized로 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 에러 정보를 담을 HashMap 생성
        HashMap<String, Object> errorInfo = new HashMap<>();
        // 예외 코드를 "code" 키에 추가
        errorInfo.put("code", exceptionCode.getCode());

        // 예외 메시지를 "message" 키에 추가
        errorInfo.put("message", exceptionCode.getMessage());

        // Gson 객체를 사용하여 HashMap을 JSON 문자열로 변환
        Gson gson = new Gson();
        String responseJson = gson.toJson(errorInfo);

        // 변환된 JSON 문자열을 응답으로 출력
        response.getWriter().print(responseJson);
    }
}
