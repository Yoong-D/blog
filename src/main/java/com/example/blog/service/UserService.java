package com.example.blog.service;

import com.example.blog.domain.Role;
import com.example.blog.filter.UserContext;
import com.example.blog.domain.User;
import com.example.blog.repository.RoleRepository;
import com.example.blog.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Setter
@Getter
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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
    public User addUser(User user){
        //role 추가 - 기본적으로 생성될 때는 일반 유저 권한 부여
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(userRole));
        return userRepository.save(user);
    }


}
