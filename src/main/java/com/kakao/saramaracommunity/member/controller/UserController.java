package com.kakao.saramaracommunity.member.controller;

import com.kakao.saramaracommunity.member.service.UserService;
import com.kakao.saramaracommunity.member.dto.SignUpDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    /*public UserController(UserService userService) {
        this.userService = userService;
    }*/

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }

    // /signup 에 대한 POST 요청시 UserDto 를 매개변수로 받아서 UserService 의 signup() 메서드 호출하는 메서드
    @PostMapping("/signup")
    public ResponseEntity<SignUpDto> signup(
            @Valid @RequestBody SignUpDto userDto
    ) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    // /user 로의 GET 요청에 대해서 @PreAuthorize 로 USER, ADMIN 두 가지 권한을 모두 허용
    // 사용자 자신의 정보를 확인 하는 메서드
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<SignUpDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    // /user/사용자 이름 에 대한 GET 요청에 대해서는 ADMIN 관리자만 허용
    // 모든 사용자 이름으로 사용자의 정보를 조회할 수 있는 메서드
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<SignUpDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }
}
