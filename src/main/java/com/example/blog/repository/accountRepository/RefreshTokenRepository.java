package com.example.blog.repository.accountRepository;

import com.example.blog.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {


    // 토근 조회(존재 확인)
    Optional<RefreshToken> findByValue(String value);
}
