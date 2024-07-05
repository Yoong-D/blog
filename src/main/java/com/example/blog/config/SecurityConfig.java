package com.example.blog.config;

import com.example.blog.security.CustomUserDetailsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity // Spring Security 설정 활성화
@RequiredArgsConstructor
public class SecurityConfig {
    public final CustomUserDetailsService customUserDetailsService;


    @Bean
    // HTTP 요청에 대한 보안 필터 체인 정의(설정)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 요청에 대한 인가 규칙 정의
                .authorizeHttpRequests(authorize -> authorize
                        // 해당 URL 요청은 모든 접근 허용
                        // Spring Boot 2.x 들어오면서 보안향상을 위해 static resouce 까지 보호 -> Spring security에서 무시할  url설정
                        .requestMatchers("/","/css/**","/scripts/**","/plugin/**","/fonts/**" ).permitAll()
                        .requestMatchers("/loginform", "/login", "/@{username}", "/mypage").permitAll()
                        .requestMatchers("/signup", "/userreg", "/welcome", "/api/users/check-username", "/api/users/check-email").permitAll()
                        .anyRequest().authenticated()
                )
                // 로그인 설정
                .formLogin(form -> form
                        .loginPage("/loginform") // /loginform 경로에 있는 커스텀 로그인 페이지를 사용하겠다
                        .loginProcessingUrl("/login") // 로그인 요청을 처리할 URL 설정 ->  controller 구현 필요 x(Spring security가 알아서 구현)
                        .defaultSuccessUrl("/@{username}", true) // 로그인 성공시 리디렉션할 URL 설정
                        .permitAll() // /loginform, login, welcome에 대한 접근 모두 허용
                )
                // 로그아웃 설정: 세션 무효화, 인증 토큰 삭제, 쿠기 정보 삭제 -> controller 구현 필요 x(Spring security가 알아서 구현)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/loginform")
                        .invalidateHttpSession(true) // 세션 무효화 설정
                        .deleteCookies("JSESSIONID", "remember-me") // 세션 쿠키와 기타 쿠키 삭제

                )
                // 동시 세션 관리 설정
                .sessionManagement(sessionManagement -> sessionManagement
                        .maximumSessions(1) // 최대 동시 세션수 설정
                        .maxSessionsPreventsLogin(true) // 최대 세션수 초과 사용자가 로그인시 로그인 차단
                )
                // 사용자 정보를 로드하는 UserDetailsService 설정 -> Spring Security가 로그인 여부를 판단하는 근거
                .userDetailsService(customUserDetailsService)
                // CSRF 보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 리멤버 미 구현
                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds(300) //세션 유지 시간 300초
                );
        // 설정 완료된 HttpSecurity으로 보안 필터 체인을 생성하여 반환
        return http.build();
    }

    @Bean
    // Spring Security에서 제공하는 비밀번호 암호화 구현체들 중  BCryptPasswordEncoder  인스턴스 생성
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    //Spring 애플리케이션에서 HTTP 세션 이벤트(예: 세션 생성 및 소멸)를 처리 -> 동시 세션 제어 - 동일 브라우저에서 로그아웃 정채 미적용 해결
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

}
