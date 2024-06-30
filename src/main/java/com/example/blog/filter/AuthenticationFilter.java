package com.example.blog.filter;


import com.example.blog.domain.User;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String path = request.getRequestURI(); // 현재 요청의 URL 가져오기
            String contextPath = request.getContextPath(); // 웹의 루트 경로 가져오기
            Cookie[] cookies = request.getCookies();
            String username = null; // 쿠키에 담겨 있는 아이디를 담을 변수
            String userid = null; // http://도메인/@{useranme}에서 username을 담을 변수

            // 요청에 들어있는 쿠키들 중 사용자를 담은 쿠키 찾기
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user")) { // 쿠키에 해당 사용자가 있다면
                        username = cookie.getValue(); // 해당 쿠키의 값으로 들어 있던 아이디 담기
                        User user = new User();
                        user.setUsername(username);
                        UserContext.setUser(user); // 쓰레드 로컬에 해당 유저(값)를 저장
                        break;
                    }
                }
            }

            //요청 URL에서 "/@"로 시작하는 부분을 찾기 -> http://도메인/@{useranme}
            if(path.startsWith(contextPath + "/@")){
                // /을 기준으로 분할
                String[] pathParts = path.split("/");
                // 적어도 /@뒤에 한개 이상의 추가 경로 세그먼트가 있는지 검사 -> username이 있는지 검사
                if(pathParts.length > 1){
                    userid = pathParts[1]; // url에서의 @username 가져오기
                    userid = userid.replace("@",""); // @를 삭제하고, usernamㄷ 가져오기
                }
            }
            //http://도메인/@{useranme}에서의 username과 로그인한 사용자의 username이 동일하다면 상세페이지 보여주기
            if(username !=null && path.equals("/@"+userid) && username.equals(userid)){
                log.info(userid+" 상세 페이지 접근 허용");
                chain.doFilter(servletRequest, servletResponse); // 다음 필터로 요청 전달
                return;
            }

            // 만약 동일하지 않다면, 접근 제한 페이로 이동
            if(path.equals("/@"+userid)){
                log.info("미 로그인 또는 다른 사용자 상세 페이지 접근 차단");
                response.sendRedirect("/access-denied");
                return;
            }
            chain.doFilter(servletRequest, servletResponse); // 다음 필터로 요청 전달
        } finally {
            UserContext.clear(); // 쓰레드 로컬 반납하기
        }


    }

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
