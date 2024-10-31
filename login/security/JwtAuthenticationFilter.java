package com.eatda.login.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 1. 요청 헤더로부터 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);

        // 2. 검증
        if (token != null && jwtProvider.validateToken(token)) {
            // If the token is valid, retrieve the Authentication object and store it in SecurityContext
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // FilterChain
        chain.doFilter(request, response);
    }

    /**
     * request header로부터 토큰 정보 추출
     * @param request
     * @return 토큰이 없으면 null 반환
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // "Bearer " 토큰이어야 함
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // 'bearer '은 제거
        }
        return null;
    }
}
