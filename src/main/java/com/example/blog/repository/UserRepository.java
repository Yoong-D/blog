package com.example.blog.repository;

import com.example.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static net.sf.jsqlparser.parser.feature.Feature.select;

public interface UserRepository extends JpaRepository<User,Long> {
    // 해당 아이디를 가진 사용자 찾기
    Optional<User> findByUsername(String username);

    // 사용자 or 이메일이 존재하는가? -> 중복 체크
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // JPQL : 해당 아이디에 매칭되는 패스워드 찾기
    @Query("select u.password from User u where u.username = :username")
    String correctPassword(@Param("username")String username); // 메서드 파라미터와 JPA 파라미터 매핑




}
