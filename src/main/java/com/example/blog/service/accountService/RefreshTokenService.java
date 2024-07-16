package com.example.blog.service.accountService;


import com.example.blog.domain.RefreshToken;
import com.example.blog.repository.accountRepository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    //refreshToken db에 저장
    @Transactional(readOnly = false)
    public RefreshToken addrefreshToken(RefreshToken refreshToken){
        return refreshTokenRepository.save(refreshToken);
    }

    //refreshToken db에서 조회
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findRefreshToken(String refreshToken){
        return refreshTokenRepository.findByValue(refreshToken);
    }

    //refreshToken db에서 삭제
    @Transactional(readOnly = false)
    public void deleteRefreshToken(String refreshToken){
        log.info("DB : refreshToken 삭제 ");
        refreshTokenRepository.findByValue(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

}
