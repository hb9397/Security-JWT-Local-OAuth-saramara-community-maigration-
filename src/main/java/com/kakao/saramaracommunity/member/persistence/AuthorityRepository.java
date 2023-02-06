package com.kakao.saramaracommunity.member.persistence;

import com.kakao.saramaracommunity.member.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, String> {
}
