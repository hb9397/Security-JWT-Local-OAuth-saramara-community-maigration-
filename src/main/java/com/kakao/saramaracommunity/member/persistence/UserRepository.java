package com.kakao.saramaracommunity.member.persistence;

import com.kakao.saramaracommunity.member.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

   // username 을 기준으로 UserEntity 정보를 가져올 때, AuthorityEntity 와 UserEntity 에 대응 되는 DB 객체를 조인해서 생성한
   // user_authorities 의 정보를 가져온다.
   // @EntityGraph 는 쿼리 수행시 지연로딩 방식이 아니라 Eager 방식으로 authorities 정보를 가져오게 된다.
   @EntityGraph(attributePaths = "role") // 아래의 쿼리 메서드 실행시 roleSet을 지연로딩 하지 않고 같이 가져오도록 지정
   @Query("select m from UserEntity m where m.email = :email and m.type = 'LOCAL' ")
   Optional<UserEntity> getWithRoles(String email);

}
