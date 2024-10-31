package com.eatda.login.security;

import com.eatda.login.form.JWToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtGenerator {

    /** <JWT 토큰 액세스 및 생성 담당>
     * 토큰 발행시, JwtDto에 담아서 반환하도록 구현
     * 이를 컨트롤러 단에서 응답엔티티 반환할 때 헤더에 액세스토큰을 담아 보낼 예정.
     * 참고: 메소드 체이닝을 통해 토큰을 생성.
     */

    private final Key key;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 3600000; // 1 hour
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 604800000; // 7 days
    private static final String GRANT_TYPE = "Bearer"; // JWT token type

    public JwtGenerator(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JWToken generateToken(Long userId, List<String> roles) {
        long now = (new Date()).getTime();

        // Access Token
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("auth", String.join(",", roles))
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        // Refresh Token
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return JWToken.builder()
                .grantType(GRANT_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

