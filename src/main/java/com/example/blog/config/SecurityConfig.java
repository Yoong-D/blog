package com.example.blog.config;

import com.example.blog.jwt.token.JwtTokenizer;
import com.example.blog.jwt.exception.CustomAuthenticationEntryPoint;
import com.example.blog.jwt.filter.JwtAuthenticationFilter;
import com.example.blog.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // Spring Security 설정 활성화
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Bean
    // HTTP 요청에 대한 보안 필터 체인 정의(설정)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 요청에 대한 인가 규칙 정의
                .authorizeHttpRequests(authorize -> authorize
                        // 해당 URL 요청은 모든 접근 허용
                        // Spring Boot 2.x 들어오면서 보안향상을 위해 static resouce 까지 보호 -> Spring security에서 무시할  url설정
                        .requestMatchers("/","/css/**","/scripts/**","/plugin/**","/fonts/**", "/img/**" ).permitAll()
                        .requestMatchers("/loginform", "/api/login", "/@{username}", "/mypage", "api/refreshToken","/logout","/@{username}/{title}","/recent", "/search").permitAll()
                        .requestMatchers("/signup", "/api/userreg", "/welcome", "/api/users/check-username", "/api/users/check-email").permitAll()
                        .anyRequest().authenticated()
                )
                //
                // UsernamePasswordAuthenticationFilter 앞에 JwtAuthenticationFilter 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenizer), UsernamePasswordAuthenticationFilter.class)
                // Spring security가 제공하는 로그인폼 비활성화
                .formLogin(form -> form.disable())
                //  Spring security가 제공하는 로그아웃 비활성화
                .logout(logout -> logout.disable())
                // 세션 관리 정책 설정 - 세션을 생성하지 않도록 지정
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // CSRF 보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                //  HTTP Basic Authentication을 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())
                // CORS 설정
                .cors(cors -> cors.configurationSource(configurationSource()))
                // 커스텀 예외처리  - 내가 구현한 예외처리로 사용
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)//인증 진입점 설정
                        .accessDeniedPage("/error2") // 접석 거부 상황에서 리다이렉트할 페이지 설정
                ); // 접근 거부 페이지 설정
        // 설정 완료된 HttpSecurity으로 보안 필터 체인을 생성하여 반환
        return http.build();
    }

    // CorsConfigurationSource를 통해 CORS 설정 구성
    public CorsConfigurationSource configurationSource() {
        // UrlBasedCorsConfigurationSource 객체 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // CorsConfiguration 객체 생성
        CorsConfiguration config = new CorsConfiguration();

        // 모든 Origin(*), 모든 Header(*), 모든 Method(*)을 허용하는 CORS 설정
        config.addAllowedOrigin("*");       // 모든 Origin 허용
        config.addAllowedHeader("*");       // 모든 Header 허용
        config.addAllowedMethod("*");       // 모든 Method 허용
        config.setAllowedMethods(List.of("GET", "POST", "DELETE")); // 특정 Method들만 허용

        // 모든 URL 패턴에 대해 위에서 설정한 CORS 설정을 적용
        source.registerCorsConfiguration("/**", config);

        // CORS 설정이 적용된 CorsConfigurationSource 반환
        return source;
    }

    @Bean
    // Spring Security에서 제공하는 비밀번호 암호화 구현체들 중  BCryptPasswordEncoder  인스턴스 생성
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
