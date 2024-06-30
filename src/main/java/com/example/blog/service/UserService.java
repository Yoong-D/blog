package com.example.blog.service;

import com.example.blog.filter.UserContext;
import com.example.blog.domain.User;
import com.example.blog.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Setter
@Getter
public class UserService {
    private final UserRepository userRepository;

    public Boolean link = false;

    // 사용자 정보 조회
    @Transactional
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    // 개인페이지가 본인의 개인 페이지가 맞는가?
    @Transactional
    public boolean userPage(String username, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies !=null){
            for(Cookie cookie : cookies){
                if(cookie.getValue().equals(username)){
                    return true;
                }
            }
        }
        return false;
    }
    // 회원 가입 완료 시 유저 추가
    @Transactional
    public void addUser(User user){
        userRepository.save(user);
    }

    //이름 중복 확인
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
