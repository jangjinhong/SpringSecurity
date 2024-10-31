package com.eatda.login.repository;

import com.eatda.login.domain.BlacklistedJWToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BlacklistedJWTokenRepository extends JpaRepository<BlacklistedJWToken, Long> {
    void deleteByExpirationTimeBefore(LocalDateTime now);

    boolean existsByToken(String token);
}
