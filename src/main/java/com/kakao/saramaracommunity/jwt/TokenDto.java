package com.kakao.saramaracommunity.jwt;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    //private String token;
    private String accessToken;
    private String refreshToken;
    private String grantType; // JWT 인증 탑으로 Bearer 사용


}
