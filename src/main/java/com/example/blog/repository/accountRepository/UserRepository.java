package com.example.blog.repository.accountRepository;

import com.example.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 아이디 조회
    User findByUsername(String username);


    // 해당 id가 이미 존재 하는가?
    boolean existsByUsername(String username);

    // 해당 email이 이미 존재하는가?
    boolean existsByEmail(String email);


}
