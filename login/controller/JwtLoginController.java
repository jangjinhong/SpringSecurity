package com.eatda.login.controller;

import com.eatda.login.form.ChildJoinRequest;
import com.eatda.login.form.LoginRequest;
import com.eatda.login.form.PresidentJoinRequest;
import com.eatda.login.form.SponsorRequest;
import com.eatda.login.service.JwtLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt-login/")
public class JwtLoginController {

    private final JwtLoginService jwtLoginService;


    /** ========== President join/login ========== */

    @PostMapping("join/president")
    public ResponseEntity<String> joinPresident(@RequestBody PresidentJoinRequest joinRequest) {
        try {
            jwtLoginService.joinPresident(joinRequest);
            return ResponseEntity.ok("President 가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("login/president")
    public ResponseEntity<String> loginPresident(@RequestBody LoginRequest loginRequest) {
        try {
            String token = jwtLoginService.loginPresident(loginRequest);
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /** ========== Child join/login ========== */

    @PostMapping("join/child")
    public ResponseEntity<String> joinChild(@RequestBody ChildJoinRequest joinRequest) {
        try {
            jwtLoginService.joinChild(joinRequest);
            return ResponseEntity.ok("Child 가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("login/child")
    public ResponseEntity<String> loginChild(@RequestBody LoginRequest loginRequest) {
        try {
            String token = jwtLoginService.loginChild(loginRequest);
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    /** ========== Sponsor join/login ========== */

    @PostMapping("join/sponsor")
    public ResponseEntity<String> joinSponsor(@RequestBody SponsorRequest joinRequest) {
        try {
            jwtLoginService.joinSponsor(joinRequest);
            return ResponseEntity.ok("Sponsor 가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("login/sponsor")
    public ResponseEntity<String> loginSponsor(@RequestBody LoginRequest loginRequest) {
        try {
            String token = jwtLoginService.loginSponsor(loginRequest);
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
