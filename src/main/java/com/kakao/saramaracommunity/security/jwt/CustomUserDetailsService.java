package com.kakao.saramaracommunity.security.jwt;

import com.kakao.saramaracommunity.member.dto.SecurityMemberDto;
import com.kakao.saramaracommunity.member.entity.UserEntity;
import com.kakao.saramaracommunity.member.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Component("userDetailsService")
@RequiredArgsConstructor
// UserDetailService 를 implements 받아서 loadUserByUsername 메서드 재정의
public class CustomUserDetailsService implements UserDetailsService {

   // UserRepository 를 생성자 주입을 통해 주입 받는다.
   private final UserRepository userRepository;

   /*public CustomUserDetailsService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }*/

   @Override
   @Transactional
   // 로그인 시 DB에서 User의 정보와 해당 User의 정보를 가져오는데 권한정보를 같이 가져와서 두 개의 정보를 이용해서 Spring Security 권한 설정에 이용되는
   // userdetails.User 객체를 생성해서 반환하고, DB에 정보가 없는 경우 예외를 발생 시킨다.
   // JPA 에서 제공하는 쿼리 메서드는 두 개 이상의 테이블에서 작업을 하는 경우
   // 각 각 의 테이블에 커넥션해서 작업하게 되는데 JPA 에서는 한번에 하나의 커넥션만 수행하기 때문에 트랜젝션 설정을 하지 않는다면 에러가 발생하게 된다.
   /*public UserDetails loadUserByUsername(final String username) {
      return userRepository.findOneWithAuthoritiesByUsername(username)
         .map(user -> createUser(username, user))
         .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
   }*/

   public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
      log.info("loadUserByUsername: " + email);

      Optional<UserEntity> result = userRepository.getWithRoles(email);
      if (result.isEmpty()) { // 유저가 없다면
         throw new UsernameNotFoundException("없는 사용자 이름");
      }

      // 존재하는 사용자 찾아오기
      UserEntity userEntity = result.get();

      SecurityMemberDto temp = SecurityMemberDto.builder()
          .email(userEntity.getEmail())
          .nickname(userEntity.getNickname())
          .type(userEntity.getType())
          .profileImage(userEntity.getProfileImage())
          .refreshToken(userEntity.getRefreshToken())
          .authorities(userEntity.getRole().stream().map(userRole ->
              new SimpleGrantedAuthority("ROLE_" + userRole.name())
          ).collect(Collectors.toList()))
          .build();
      log.info(userEntity);
      log.info(temp.toString());

      List<GrantedAuthority> authorities = userEntity.getRole().stream().map(userRole ->
          new SimpleGrantedAuthority("ROLE_" + userRole.name())
      ).collect(Collectors.toList());

      return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
   }


/*   private org.springframework.security.core.userdetails.User createUser(String username, UserEntity user) {
      // 레포지토지토리 부터 받아온 DB 의 사용자의 정보를 가져와서 activate 되어 있지 않으면 예외를 발생 시키고
      if (!user.isActivated()) {
         throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
      }

      // 정상적으로 사용자의 정보를 가져온 경우 사용자 정보에서 권한 정보를 가지고 Security 에서 권한을 인지하는데 사용되는 GrantedAuthority 객체를 생성
      List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
              .collect(Collectors.toList());

      // 사용자 정보에서 사용자 이름, 비밀번호, 권한정보를 가지고  Spring Security 인가에 이용되는 userdetails.User 객체 반환
      return new org.springframework.security.core.userdetails.User(user.getUsername(),
              user.getPassword(),
              grantedAuthorities);
   }*/
}
