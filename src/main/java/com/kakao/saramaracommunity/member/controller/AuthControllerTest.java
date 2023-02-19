package com.kakao.saramaracommunity.member.controller;

import com.kakao.saramaracommunity.common.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthControllerTest {

    /*private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response, @RequestBody String accessToken) {
        return ResponseEntity.ok().body(authService.refreshToken(request, response, accessToken));
    }*/

    @GetMapping(value = "/token")
    public ResponseEntity token(@RequestParam String accessToken,
                                @RequestParam String refreshToken) {

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();

        return new ResponseEntity<>( tokenDto, HttpStatus.OK);

    }
}
