package com.example.blog.security;

import com.example.blog.domain.User;
import com.example.blog.repository.accountRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
// 사용자 인증을 처리하기 위해 사용자 정보를 로드
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    // 사용자 이름(일반적으로 사용자 ID 또는 이메일)을 기반으로 사용자 정보를 검색
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        log.info("사용자명!!!!!!!!!!!!!!!!!!!!" + user.getName());
        // 사용자가 없다면 예외 발생
        if(user != null){
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }
//        // UserBuilder : Spring Security에서 UserDetails 객체 쉽게 생성해주는 것 -> 사용자 정보 설정 가능
//        // User.withUsername(username)를 호출하여 username으로 UserBuilder 객체를 생성
//        org.springframework.security.core.userdetails.User.UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
//        userBuilder.password(user.getPassword()); // 비밀번호 설정
//        userBuilder.roles(user.getRoles().stream().map(role -> role.getName()).toArray(String[]::new)); // 권한 설정
//        // build()를 통해 UserBuilder 객체에서 설정한 정보를 기반으로 UserDetails 객체를 생성
//        return userBuilder.build();
        List<String> roles = Arrays.asList(user.getRoles().stream()
                .map(role -> role.getName())
                .toArray(String[]::new));

        return new CustomUserDetails(user.getName(),user.getUsername(),user.getPassword(),roles);
    }
}