package com.kakao.saramaracommunity.member.service;


import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kakao.saramaracommunity.member.dto.MemberDto;
import com.kakao.saramaracommunity.member.dto.SecurityMemberDto;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;

import org.springframework.transaction.annotation.Transactional;

public interface UserService {


    // 회원가입 메서드
    // 회원가입 시 사용자가 입력한 정보에 대응되는 UserDto 를 받아 DB에서 User의 정보와 해당 User의 정보가 존재하는지 확인하는데
    // JPA 에서 제공하는 쿼리 메서드는 두 개 이상의 테이블에서 작업을 하는 경우
    // 각 각 의 테이블에 커넥션해서 작업하게 되는데 JPA 에서는 한번에 하나의 커넥션만 수행하기 때문에 트랜젝션 설정을 하지 않는다면 에러가 발생하게 된다.
    @Transactional
    public SecurityMemberDto signup(SecurityMemberDto securityMemberDto);

    // 회원정보 수정 메서드
    public MemberDto infoChange(MemberDto memberDto);


    // Member 를 SecurityMemberDto로 변환하는 메서드
    default SecurityMemberDto securityDtoFrom(Member member){
        if(member == null) return null;

        return SecurityMemberDto.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .role(member.getRole().stream()
                .map(role1 -> Role.USER)
                .collect(Collectors.toSet()))
            .picture(member.getPicture())
            .type(member.getType())
            .build();
    }

    default Member entityFromSecurityDto(SecurityMemberDto securityMemberDto, String password){
        return Member.builder()
            .email(securityMemberDto.getEmail())
            .password(password)
            .nickname(securityMemberDto.getNickname())
            .type(Type.LOCAL)
            .role(Collections.singleton(Role.USER))
            .picture(securityMemberDto.getPicture())
            .build();
    }

    // MemberDto를 Member 로 변환하는 메서드
    default Member entityFrom(MemberDto memberDto){
        return Member.builder()
            .password(memberDto.getPassword())
            .nickname(memberDto.getNickname())
            .picture(memberDto.getPicture())
            .build();

    }

    // Member를 MemberDto로 변환하는 메서드
    default MemberDto dtoFrom(Member member){
        if(member == null) return null;

        return MemberDto.builder()
            .email(member.getEmail())
            .password(member.getPassword())
            .nickname(member.getNickname())
            .picture(member.getPicture())
            .type(member.getType())
            .build();
    }

    // email 을 매개변수로 사용자 정보와 권한 정보를 가져오는 메서드인데 SecurityContext 가
    @Transactional(readOnly = true)
    public SecurityMemberDto getUserWithAuthorities(String email);

    // 현재 Security Context 에 저장된 username 의 사용자 정보, 권한정보 만을 가져오는 메서드
    @Transactional(readOnly = true)
    public SecurityMemberDto getMyUserWithAuthorities();
}
