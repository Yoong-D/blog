package com.example.blog.service;

import com.example.blog.config.UserContext;
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


    // 로그인 구현
    @Transactional
    public boolean user(String userName, String password){
        if(userRepository.findByUsername(userName).isPresent()){ // 해당 아이디가 DB에 존재하는가?
            if(userRepository.correctPassword(userName).equals(password)){ // 해당 아이디에 매핑되는 패스워드와, 입력받은 패스워드가 동일한가?
                // 로그인 완료시 userHolder에 현재 쓰레드와 연관된 사용자 정보(아이디)를 저장
                UserContext.setUser(userName);
                return true;
            }else{
                return false;
            }

        }else{ // 해당 아이디가 DB에 존재하지 않으면 무조건 로그인 실패
            return false;
        }
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
    //이메일
    @Transactional
    public boolean isEmailExists(String email) {
        // 데이터베이스에 사용자 중복 여부를 확인하는 로직
        return userRepository.existsByEmail(email);
    }

}
