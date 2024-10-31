package com.eatda.login.controller;

import com.eatda.global.exception.CustomException;
import com.eatda.global.exception.ErrorCode;
import com.eatda.login.security.JwtProvider;
import com.eatda.login.service.JwtLogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt-logout/")
public class JwtLogoutController {

    private final JwtLogoutService jwtLogoutService;
    private final JwtProvider jwtProvider;

    @PostMapping("")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            Date expiration = jwtProvider.getExpirationDate(token); // 만료 시간 가져오기

            // 로그아웃 처리 (JWT 블랙리스트에 저장)
            jwtLogoutService.logout(token, expiration);
            return ResponseEntity.ok("로그아웃 완료");

        } catch (CustomException e) {
            if (e.getErrorCode() == ErrorCode.TOKEN_ALREADY_LOGGED_OUT) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorCode().getMessage());
            }
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 처리 중 오류가 발생했습니다.");
        }
    }

}
