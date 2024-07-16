package com.example.blog.jwt.filter;

import com.example.blog.jwt.token.JwtTokenizer;
import com.example.blog.jwt.exception.JwtExceptionCode;
import com.example.blog.jwt.token.JwtAuthenticationToken;
import com.example.blog.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;

    // HTTP 요청에서 JWT 토큰 추출하기
    public String getToken(HttpServletRequest request) {
        // HTTP 요청 헤더에서 Authorization(권한) 값 추출
        String authorization = request.getHeader("Authorization");

        // 서버로 인증 토큰 전달하는 방법 1
        // Authorization 헤더가 존재하고, "Bearer "로 시작하는 경우
        // 즉 클라이언트가 Bearer 스키마를 사용하여 인증 토큰을 전송하고 있을 경우
        // 인증 토큰 값과의 구분이 필요하므로 Bearer 뒤에 공백 하나 두기
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            // "Bearer " 부분을 제외한 토큰 값을 반환
            return authorization.substring(7);
        }

        // 서버로 인증 토큰 전달하는 방법 2 -> 쿠키로 전송하고 있는 경우
        // HTTP 요청 쿠키에서 accessToken 쿠키 값 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 쿠키의 이름이 "accessToken"인 경우 해당 쿠키의 값을 반환
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // 만약 헤더나 쿠키에서 토큰 값을 찾지 못한 경우 null을 반환
        return null;
    }
    // Claims 객체에서 사용자의 권한 추출하여 리스트로 반환
    private List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
        // 클레임에서 "roles" 정보를 추출(사용자가 가지고 있는 권한 목록)
        List<String> roles = (List<String>) claims.get("roles");

        // 권한 정보를 담을 리스트를 생성
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 각 역할(role)을 GrantedAuthority 객체로 변환하여 리스트에 추가
        for (String role : roles) {
            authorities.add(() -> role);
        }
        // 권한 목록을 반환
        return authorities;
    }

    // getToken()을 통해 사용자의 인증 정보를 가져오고,
    // JWT에서 추출한 Claim으로 getGrantedAuthorities()로 권한을 가져와 SecurityContextHolder 설정
    private void getAuthentication(String token) {
        // JWT 토큰을 파싱하여 클레임(claim)들을 추출합니다.
        Claims claims = jwtTokenizer.parseAccessToken(token);

        // 클레임에서 사용자 정보 추출
        String email = claims.getSubject(); // 주로 이메일이나 유저 아이디
        Long userId = claims.get("userId", Long.class); // 사용자 ID
        String name = claims.get("name", String.class); // 사용자 이름
        String username = claims.get("username", String.class); // 사용자 아이디 (username)

        // JWT에서 추출한 Claim으로 토큰에 권한 추출
        List<GrantedAuthority> authorities = getGrantedAuthorities(claims);

        // CustomUserDetails 객체 생성
        CustomUserDetails userDetails = new CustomUserDetails(username, "", name,
                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        // Authentication 생성 (권한 목록, 사용자 정보, 인증여부)
        Authentication authentication = new JwtAuthenticationToken(authorities, userDetails, null);

        // SecurityContextHolder에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    // 필터링 로직 구현
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Http 요청을에서 accessToken 추출
        String token = getToken(request);

        // token이 null이나, 공백이 아니라면  -> accessToken이 존재한다면
        if (StringUtils.hasText(token)) {
            try {
                // accessToken을 사용하여 사용자 인증 정보를 가져오는 메서드를 호출
                getAuthentication(token);
            } catch (ExpiredJwtException e) {
                // 토큰이 만료된 경우 처리
                request.setAttribute("exception", JwtExceptionCode.EXPIRED_TOKEN.getCode());
                log.error("Expired Token : {}", token, e);
                throw new BadCredentialsException("Expired token exception", e); // BadCredentialsException으로 예외 넘기기
            } catch (UnsupportedJwtException e) {
                // 지원되지 않는 토큰 형식인 경우 처리
                request.setAttribute("exception", JwtExceptionCode.UNSUPPORTED_TOKEN.getCode());
                log.error("Unsupported Token: {}", token, e);
                throw new BadCredentialsException("Unsupported token exception", e); // BadCredentialsException으로 예외 넘기기
            } catch (MalformedJwtException e) {
                // 잘못된 형식의 토큰인 경우 처리
                request.setAttribute("exception", JwtExceptionCode.INVALID_TOKEN.getCode());
                log.error("Invalid Token: {}", token, e);
                throw new BadCredentialsException("Invalid token exception", e); // BadCredentialsException으로 예외 넘기기
            } catch (IllegalArgumentException e) {
                // 토큰이 발견되지 않은 경우 처리
                request.setAttribute("exception", JwtExceptionCode.NOT_FOUND_TOKEN.getCode());
                log.error("Token not found: {}", token, e);
                throw new BadCredentialsException("Token not found exception", e); // BadCredentialsException으로 예외 넘기기
            } catch (Exception e) {
                // 그 외의 예외 발생 시 처리
                log.error("JWT Filter - Internal Error: {}", token, e);
                throw new BadCredentialsException("JWT filter internal exception", e); // BadCredentialsException으로 예외 넘기기
            }
        }

        // 다음 필터 체인으로 요청과 응답 객체를 전달
        filterChain.doFilter(request, response);
    }
}
