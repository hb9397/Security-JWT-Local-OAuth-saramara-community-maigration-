package com.kakao.saramaracommunity.member;

import com.kakao.saramaracommunity.jwt.Auth.AuthorityEntity;
import com.kakao.saramaracommunity.jwt.exception.DuplicateMemberException;
import com.kakao.saramaracommunity.jwt.exception.NotFoundMemberException;
import com.kakao.saramaracommunity.jwt.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }*/

    @Transactional
    // 회원가입 메서드
    // 회원가입 시 사용자가 입력한 정보에 대응되는 UserDto 를 받아 DB에서 User의 정보와 해당 User의 정보가 존재하는지 확인하는데
    // JPA 에서 제공하는 쿼리 메서드는 두 개 이상의 테이블에서 작업을 하는 경우
    // 각 각 의 테이블에 커넥션해서 작업하게 되는데 JPA 에서는 한번에 하나의 커넥션만 수행하기 때문에 트랜젝션 설정을 하지 않는다면 에러가 발생하게 된다.
    public UserDto signup(UserDto userDto) {
        // 이미 입력한 정보에 대한 회원이 있을 때 DuplicateMemberException 예외 발생
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw DuplicateMemberException.builder().message("이미 가입되어 있는 유저입니다.").build();
        }


        // 중복된 정보의 사용자 정보와 사용자에 대한 권한 정보가 없다면
        // 해당 사용자에게 USER 권한 부여
        AuthorityEntity authority = AuthorityEntity.builder()
                .authorityName("ROLE_USER")
                .build();
        // 사용자 정보를 DB 에 저장
        UserEntity user = UserEntity.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return UserDto.from(userRepository.save(user));
    }

    // username 을 매개변수로 사용자 정보와 권한 정보를 가져오는 메서드인데
    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    // 현재 Security Context 에 저장된 username 의 사용자 정보, 권한정보 만을 가져오는 메서드
    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() ->  NotFoundMemberException.builder().message("Member not found").build())
        );
    }
}
