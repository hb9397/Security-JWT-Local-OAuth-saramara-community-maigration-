package com.kakao.saramaracommunity.member.controller;

import com.kakao.saramaracommunity.member.dto.MemberDto;
import com.kakao.saramaracommunity.member.dto.SecurityMemberDto;
import com.kakao.saramaracommunity.member.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final UserService userService;

    // /signup 에 대한 POST 요청시 UserDto 를 매개변수로 받아서 UserService 의 signup() 메서드 호출하는 메서드
    @PostMapping("/signup")
    public ResponseEntity<SecurityMemberDto> signup(
        @Valid @RequestBody SecurityMemberDto securityMemberDto
    ) {
        return ResponseEntity.ok(userService.signup(securityMemberDto));
    }

    @PostMapping("/infochange")
    public ResponseEntity<MemberDto> infoChange(
        @Valid @RequestBody MemberDto memberDto
    ){
        return null;
    }

    // /user 로의 GET 요청에 대해서 @PreAuthorize 로 USER, ADMIN 두 가지 권한을 모두 허용
    // 사용자 자신의 정보를 확인 하는 메서드
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<SecurityMemberDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());

    }

    // /user/사용자 이름 에 대한 GET 요청에 대해서는 ADMIN 관리자만 허용
    // 모든 사용자 이름으로 사용자의 정보를 조회할 수 있는 메서드
    @GetMapping("/user/{nickname}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<SecurityMemberDto> getUserInfo(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(nickname));
    }

}
