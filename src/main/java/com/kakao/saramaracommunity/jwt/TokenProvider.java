package com.kakao.saramaracommunity.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Log4j2
@Component
public class TokenProvider implements InitializingBean {
   private static final String AUTHORITIES_KEY = "auth";

   private final String secret;

   private final long tokenValidityInMilliseconds;
   private Key key;



   public TokenProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.token-validity-in-seconds}")long tokenValidityInSeconds) {
      this.secret = secret;
      this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
   }

   // InitializingBean 을 implements 해서 afterPropertiesSet 를 재정의하는 이유는
   // Bean 이 생성되고 secret 값과 만료시간을 의존성 주입 받은 뒤에 Secret 값을 Base64 Decode 한 후 key 변수에 할당하기 위함이다.
   @Override
   public void afterPropertiesSet() {
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      this.key = Keys.hmacShaKeyFor(keyBytes);
   }

   // Spring Security 의 Authentication  객체의 권한 정보를 이용해서 토큰을 생성하는 createToken 메서드
   public String createToken(Authentication authentication) { // Authentication 을 매개변수로 받아서
      // token 을 발급받기 위해 authentication 객체의 정보를 추출해서 권한정보 객체 authentication 객체를 생성
      String authorities = authentication.getAuthorities().stream()
         .map(GrantedAuthority::getAuthority)
         .collect(Collectors.joining(","));

      // token 의 만료시간을 만드는데 현재시간 + application.yaml 파일로 부터 주입 받은 만료시간 으로 생성
      long now = (new Date()).getTime();
      Date validity = new Date(now + this.tokenValidityInMilliseconds);

      // JWT를 키, 알고리즘, 만료시간을 설정하고 생성해서 반환한다.
      return Jwts.builder()
         .setSubject(authentication.getName())
         .claim(AUTHORITIES_KEY, authorities)
         .signWith(key, SignatureAlgorithm.HS512)
         .setExpiration(validity)
         .compact();
   }

   // Client로 부터 받은 token 의 정보를 이용해서 Authentication 객체를 반환하는 메서드
   public Authentication getAuthentication(String token) {
      // JWTs 객체인 token을 Claims 객체로 만들어준다.
      Claims claims = Jwts
              .parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();

      // 만들어진 Claims 객체를 이용해서 권한정보를 가지고 있는 GrantedAuthority 콜렉션 객체 생성
      Collection<? extends GrantedAuthority> authorities =
         Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

      // 만들어진 Claims 객체와 권한정보 객체를 이용해서 User 객체 생성
      User principal = new User(claims.getSubject(), "", authorities);

      // 최종적으로 Token 을 이용해서 특정 사용자의 정보를 갖는 Authentication
      // 의 일종인 UsernamePasswordAuthenticationToken 객체 생성해서 반환
      return new UsernamePasswordAuthenticationToken(principal, token, authorities);
   }


   // token을 이용해서 현재 토큰이 유효한지 검사하는 메서드
   public boolean validateToken(String token) {
      try {
         // 받아온 토큰을 Parsing 해서 예외가 생기지 않는 경우에는 true 를 반환한다.
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         return true;
      } // 예외가 발생한 경우에 따른 log를 생성하고 false 값을 반환
      catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         log.info("잘못된 JWT 서명입니다.");
      } catch (ExpiredJwtException e) {
         log.info("만료된 JWT 토큰입니다.");
      } catch (UnsupportedJwtException e) {
         log.info("지원되지 않는 JWT 토큰입니다.");
      } catch (IllegalArgumentException e) {
         log.info("JWT 토큰이 잘못되었습니다.");
      }
      return false;
   }
}
