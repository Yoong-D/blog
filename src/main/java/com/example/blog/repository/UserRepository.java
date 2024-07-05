package com.example.blog.repository;

import com.example.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    // 해당 아이디를 가진 사용자 찾기
    User findByUsername(String username);

    // 해당 id가 이미 존재 하는가?
    boolean existsByUsername(String username);

    // 해당 email이 이미 존재하는가?
    boolean existsByEmail(String email);


}
