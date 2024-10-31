package com.eatda.login.service;

import com.eatda.child.domain.Child;
import com.eatda.child.repository.ChildRepository;
import com.eatda.login.form.ChildJoinRequest;
import com.eatda.login.form.PresidentJoinRequest;
import com.eatda.login.form.SponsorRequest;
import com.eatda.login.security.JwtGenerator;
import com.eatda.login.form.LoginRequest;
import com.eatda.president.domain.President;
import com.eatda.president.repository.PresidentRepository;
import com.eatda.sponsor.domain.Sponsor;
import com.eatda.sponsor.repository.SponsorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class JwtLoginService {

    private final PresidentRepository presidentRepository;
    private final ChildRepository childRepository;
    private final SponsorRepository sponsorRepository;
    private final JwtGenerator jwtGenerator;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void joinPresident(PresidentJoinRequest joinRequest) {
        // 이메일 중복 체크
        if (presidentRepository.findByPresidentEmail(joinRequest.getPresidentEmail()).isPresent()) {
            throw new IllegalArgumentException("해당 Email은 사용할 수 없습니다.");
        }

        // 비밀번호 해시 처리
        String hashedPassword = passwordEncoder.encode(joinRequest.getPresidentPassword());

        // 새로운 President 객체 생성 및 저장
        President newPresident = President.builder()
                .businessNumber(joinRequest.getBusinessNumber())
                .presidentName(joinRequest.getPresidentName())
                .presidentEmail(joinRequest.getPresidentEmail())
                .presidentPassword(hashedPassword) // 해시 처리된 비밀번호 사용
                .presidentNumber(joinRequest.getPresidentNumber())
                .build();

        presidentRepository.save(newPresident);
    }

    public String loginPresident(LoginRequest loginRequest) {
        // Find the president by email
        President president = presidentRepository.findByPresidentEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // Check if the password matches
        if (!passwordEncoder.matches(loginRequest.getPassword(), president.getPresidentPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        // Generate JWT token
        return jwtGenerator.generateToken(president.getId(), Collections.singletonList("ROLE_PRESIDENT")).getAccessToken();
    }

    @Transactional
    public void joinChild(ChildJoinRequest joinRequest) {
        // 이메일 중복 체크
        if (childRepository.findByChildEmail(joinRequest.getChildEmail()).isPresent()) {
            throw new IllegalArgumentException("해당 Email은 사용할 수 없습니다.");
        }

        // 비밀번호 해시 처리
        String hashedPassword = passwordEncoder.encode(joinRequest.getChildPassword());

        // 새로운 President 객체 생성 및 저장
        Child newChild = Child.builder()
                .childName(joinRequest.getChildName())
                .childEmail(joinRequest.getChildEmail())
                .childNumber(joinRequest.getChildNumber())
                .childPassword(hashedPassword) // 해시 처리된 비밀번호 사용
                .childAddress(joinRequest.getChildAddress())
                .build();

        childRepository.save(newChild);
    }

    public String loginChild(LoginRequest loginRequest) {
        Child child = childRepository.findByChildEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), child.getChildPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        return jwtGenerator.generateToken(child.getId(), Collections.singletonList("ROLE_CHILD")).getAccessToken();
    }




    @Transactional
    public void joinSponsor(SponsorRequest joinRequest) {
        // 이메일 중복 체크
        if (sponsorRepository.findBySponsorEmail(joinRequest.getSponsorEmail()).isPresent()) {
            throw new IllegalArgumentException("해당 Email은 사용할 수 없습니다.");
        }

        // 비밀번호 해시 처리
        String hashedPassword = passwordEncoder.encode(joinRequest.getSponsorPassword());

        // 새로운 President 객체 생성 및 저장
        Sponsor newSponsor = Sponsor.builder()
                .sponsorName(joinRequest.getSponsorName())
                .sponsorEmail(joinRequest.getSponsorEmail())
                .sponsorPassword(hashedPassword) // 해시 처리된 비밀번호 사용
                .sponsorAddress(joinRequest.getSponsorAddress())
                .build();

        sponsorRepository.save(newSponsor);
    }

    public String loginSponsor(LoginRequest loginRequest) {
        Sponsor sponsor = sponsorRepository.findBySponsorEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), sponsor.getSponsorPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        return jwtGenerator.generateToken(sponsor.getId(), Collections.singletonList("ROLE_SPONSOR")).getAccessToken();
    }

}
