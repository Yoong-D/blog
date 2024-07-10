package com.example.blog.repository;

import com.example.blog.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    // 해당 토큰이 있냐 없냐로 확인하니까 Optional 사용
    Optional<RefreshToken> findByValue(String value);
}
