package com.kakao.saramaracommunity.member;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
   @EntityGraph(attributePaths = "authorities")
   Optional<UserEntity> findOneWithAuthoritiesByUsername(String username);
}
