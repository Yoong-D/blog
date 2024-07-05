package com.example.blog.security;

import com.example.blog.domain.User;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    public final UserRepository userRepository;

    @Override
    // 사용자 이름을 기반으로 특정 사용자의 정보를 조회하고 반환하는 메서드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // id에 해당하는 유저 조회해서 얻어오기
        User user = userRepository.findByUsername(username);

        // user에 아무 유저가 없다면 예외 발생시키기
        if (user == null) {
            throw new UsernameNotFoundException("사용자가 없습니다.");
        }
        // UserBuidler를 통해 사용자 정보 설정
        org.springframework.security.core.userdetails.User.UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
        userBuilder.password(user.getPassword());
        // 해당 사용자가 가지고 있는 역할 이름을 스트림으로 추출하고 String 배열로 반환
        userBuilder.roles(user.getRoles().stream().map(role -> role.getName()).toArray(String[]::new));
        return userBuilder.build();
    }
}
