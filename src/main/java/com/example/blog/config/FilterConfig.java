package com.example.blog.config;


import com.example.blog.filter.AuthenticationFilter;
import com.example.blog.service.UserService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilters(UserService userService) {

        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>(); // 필터를 등록하고 구성할때 사용하는 FilterRegistrationBean 클래스 객체 생성
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(); // 내가 만든 필터 객체 생성

        authenticationFilter.setUserService(userService); // 내가 만들 필터의 UserService를 setter를 이용해 의존성 주입

        registrationBean.setFilter(authenticationFilter); // 필터를 내가 만든 필터로 구성
        registrationBean.addUrlPatterns("/*"); // 모든 경로에 필터 적용
        registrationBean.setOrder(1); // 필터 실행 순서 설정 (낮을수록 먼저 실행)
        return registrationBean;

    }
}