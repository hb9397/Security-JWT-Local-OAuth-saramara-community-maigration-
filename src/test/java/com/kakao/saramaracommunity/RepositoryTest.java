package com.kakao.saramaracommunity;

import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.persistence.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void getEmailWithRole(){

        System.out.println(memberRepository.getWithRolesEqualLocal("test1@test.test"));
        System.out.println(memberRepository.getWithRolesEqualOAuth("test1@test.test", Type.LOCAL));
    }

}
