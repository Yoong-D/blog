package com.example.blog.filter;


import com.example.blog.service.UserService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthenticationFilter implements Filter {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 코드 (필요한 경우)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 요청의 URI와 HTTP 메서드를 로그에 출력
        System.out.println("AuthenticationFilter: " + httpRequest.getRequestURI());
        System.out.println("AuthenticationFilter: " + httpRequest.getMethod());

        chain.doFilter(request, response);
    }
//@Override
//public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//        throws IOException, ServletException {
//    HttpServletRequest httpRequest = (HttpServletRequest) request;
//    HttpServletResponse httpResponse = (HttpServletResponse) response;
//    // 로그인 이후 모든 요청에 대해 인터셉터가 쿠키를 확인하고, 유효한 경우 요청을 처리하며, 유효하지 않은 경우 로그인 페이지로 리다이렉트
//    // 이렇게 하면 로그인 상태를 유지하고 인증된 사용자만 특정 API나 URL에 접근할 수 있게 된다.
//    Cookie[] cookies = httpRequest.getCookies();
//    if (cookies != null) {
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("user")) {
//                String username = cookie.getValue();
//                // 사용자 검증 로직
//                //if (userService.userPage(username)) {
//                    chain.doFilter(request, response); // 검증 성공 시 요청 진행
//                    return;
//               // }
//            }
//        }
//    }
//    httpResponse.sendRedirect("/error"); // 검증 실패 시 로그인 페이지로 리다이렉트
//}
    @Override
    public void destroy() {
        // 정리 코드 (필요한 경우)
    }

    private String validateTokenAndGetUserId(String token) {
        // 토큰 검증 및 사용자 ID 추출 로직 (예: JWT 검증)
        // 유효한 경우 사용자 ID를 반환, 그렇지 않으면 null 반환
        return null;
    }
}
