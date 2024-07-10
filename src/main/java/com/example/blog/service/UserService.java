package com.example.blog.service;

import com.example.blog.domain.Role;
import com.example.blog.domain.User;
import com.example.blog.repository.RefreshTokenRepository;
import com.example.blog.repository.RoleRepository;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // 비밀번호 암호화
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // 아이디로 사용자 찾기
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    // 주키(id)로 사용자 찾기
    @Transactional(readOnly = true)
    public Optional<User> findUser(Long id){
        return userRepository.findById(id);
    }

    // 회원 가입 완료 시 유저 추가
    @Transactional
    public User addUser(User user) {
        // role 테이블에 USER이라는 역할을 Role 객체로 반환
        Role userRole = roleRepository.findByName("USER");
        // 사용자에게 역할을 설정
        // 단일 요소로 구성된 불변 집합을 생성하여 사용자아게 한 개의 역할만 설정하도록 구현
        user.setRoles(Collections.singleton(userRole));
        //password 설정 - 패스워드 인코더 사용하여 암호화하기
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 블로그 이름 설정 - 본인 id + .log
        user.setBlog_name(user.getUsername() + ".log");
        return userRepository.save(user);
    }

    // 이름 중복 확인
    @Transactional
    public boolean isUserExists(String username) {
        // 데이터베이스에 사용자 중복 여부를 확인하는 로직
        return userRepository.existsByUsername(username);
    }

    //이메일 중복 확인
    @Transactional
    public boolean isEmailExists(String email) {
        // 데이터베이스에 사용자 중복 여부를 확인하는 로직
        return userRepository.existsByEmail(email);
    }
}
