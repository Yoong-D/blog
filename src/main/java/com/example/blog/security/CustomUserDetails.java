package com.example.blog.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// 인증된 사용자의 세부 정보 구현
public class CustomUserDetails implements UserDetails {
    private final String name; // 사용자 명
    private final String username; // 사용자 id
    private final String password; // 사용자 pw
    //GrantedAuthority : 사용자의 권한을 나타내는 인터페이스 -> 사용자의 권한에 대한 정보를 담음
    // 해당 사용자가 가지고 있는 모든 권한을 담는 리스트
    private final List<GrantedAuthority> authorities;

    // 생성자
    public CustomUserDetails(String name, String username, String password, List<String> roles){
        this.name = name;
        this.username = name;
        this.password = password;
        // GrantedAuthority의 구현체는 SimpleGrantedAuthority
        this.authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


    @Override
    // 사용자의 이름을 반환
    public String getUsername() {
        return username;
    }

    @Override
    // 사용자의 패스워드 반환
    public String getPassword() {
        return password;
    }

    @Override
    // 사용자의 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    // 계정이 만료되지 않았는지 여부 반환
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았다.
    }

    @Override
    // 계정 활성화 여부 반환
    public boolean isEnabled() {
        return true; // 계정이 활성화 되어있다.
    }

    @Override
    // 자격 증명이 만료되지 않았는지 여부 반환
    public boolean isCredentialsNonExpired() {
        return true; // 계정의 자격 증명이 만료되지 않았다.
    }

    @Override
    // 계정이 잠기지 않았는지 여부 반환
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠겨있지 않다
    }
}
