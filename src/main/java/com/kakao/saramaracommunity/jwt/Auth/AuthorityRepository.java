package com.kakao.saramaracommunity.jwt.Auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, String> {
}
