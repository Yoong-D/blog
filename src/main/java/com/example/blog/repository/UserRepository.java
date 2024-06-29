package com.example.blog.repository;

import com.example.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static net.sf.jsqlparser.parser.feature.Feature.select;

public interface UserRepository extends JpaRepository<User,Long> {
    // 해당 아이디를 가진 사용자 찾기
    User findByUsername(String username);

    // 사용자 or 이메일이 존재하는가? -> 중복 체크
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);





}
