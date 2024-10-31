package com.eatda.login.service;

import com.eatda.global.exception.CustomException;
import com.eatda.global.exception.ErrorCode;
import com.eatda.login.repository.BlacklistedJWTokenRepository;
import com.eatda.login.domain.BlacklistedJWToken;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtLogoutService {

    private final BlacklistedJWTokenRepository blacklistedJWTokenRepository;

    @Transactional
    public void logout(String token, Date expiration) {
        if (blacklistedJWTokenRepository.existsByToken(token)) {
            throw new CustomException(ErrorCode.TOKEN_ALREADY_LOGGED_OUT);
        }
        BlacklistedJWToken blacklistedToken = BlacklistedJWToken.builder()
                .token(token)
                .expirationTime(expiration.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .build();

        blacklistedJWTokenRepository.save(blacklistedToken);
    }

    @Scheduled(fixedRate = 86400000) // Run once per day
    public void cleanUpExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        blacklistedJWTokenRepository.deleteByExpirationTimeBefore(now);
    }

}
