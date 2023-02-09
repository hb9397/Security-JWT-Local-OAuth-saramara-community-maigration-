package com.kakao.saramaracommunity.config;

import com.kakao.saramaracommunity.security.jwt.JwtAccessDeniedHandler;
import com.kakao.saramaracommunity.security.jwt.JwtAuthenticationEntryPoint;
import com.kakao.saramaracommunity.security.jwt.JwtSecurityConfig;
import com.kakao.saramaracommunity.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor

// 기본적인 웹 보안 시큐리티 설정하는 어노테이션
@EnableWebSecurity

// @PreAuthorize 어노테이션을 메서드 단위로 추가하기 위해서 사용한다.
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    // TokenProvider, CorsFilter, JwtAuthenticationEntryPoint, JwtAccessDeniedHandler 를 생성자 주입을 통해 주입 받는다.
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /*public SecurityConfig(
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }*/

    @Bean
    // 비밀번호 암호화를 위한 메서드
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                // ExceptionHandling 시 401, 403 에러에 대해서 예외에 대해 만든 클래스를 사용
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // enable h2-console
                /*.and()
                .headers()
                .frameOptions()
                .sameOrigin()*/

                // JWT 토큰을 사용하므로(jwt 자체가 stateless 한 상태이므로) 서버단에서 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests() // HttpServletRequest 를 사용하는 요청에 대한 접근을 제한을 설정하기 위한 메서드
                // 2가지 URL 에 대해서는 제한없이 허용하겠다는 메서드, 로그인, 회원가입 에는 토큰이 없는 상태로 접근하기 때문
                .requestMatchers("/api/authenticate", "/oauth2/**","/api/signup", "/api/login").permitAll()
                //.requestMatchers(PathRequest.toH2Console()).permitAll()
                // 위의 세가지 URL 을 제외한 요청에는 인증을 하겠다는 의미
                .anyRequest().authenticated()
                // TokenProvider 를 시큐리티 로직에 등록하는 JwtFilter 를 addFilterBefore 메서드로 등록한 JwtSecurityConfig 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));


                // oauth 로그인 시 결과를 redirect 할 URI
                httpSecurity
                        .oauth2Login()
                        .redirectionEndpoint()
                        .baseUri("/oauth2/code/*");

        return httpSecurity.build();
    }
}