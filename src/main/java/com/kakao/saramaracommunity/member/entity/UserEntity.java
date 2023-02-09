package com.kakao.saramaracommunity.member.entity;

import com.kakao.saramaracommunity.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@ToString
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update member set deleted_at = CURRENT_TIMESTAMP where member_id = ?")
@Entity
public class UserEntity extends BaseTimeEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long memberId;

   @Column(nullable = false, length = 50, unique = true)
   private String email;

   @Column(nullable = false, length = 100)
   private String password;
   @Column(nullable = false, length = 50, unique = true)
   private String nickname;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private Type type;

   @ElementCollection(fetch = FetchType.LAZY)
   @Builder.Default
   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private Set<UserRole> role = new HashSet<>();

   private String profileImage;

   private String refreshToken;


   // 회원이 정보를 수정, RefreshToken 재발급 등의 이유로 바뀔 수 있는 값들에 대한 처리
   public void changeNickname(String nickname) {this.nickname = nickname;}

   public void changePassword(String password) {this.password = password;}
   public void changeProfileImage(String profileImage) {this.profileImage = profileImage;}

   public void setRole(UserRole userRole) {this.role.add(userRole);}

   public void setType(Type type) {this.type = type;}

   public void setRefreshToken(String refreshToken) {this.refreshToken = refreshToken;}

   // UserEntity 객체와 Authority 객체를 다대다 관계를 1:N, N:1 관계의 Join Table 로 정의한다는 의미
   // @JoinColumn(name = "authority_name", referencedColumnName = "authority_name" 특정 user가 가진 자격이름을 갖는다.
   /*@ManyToMany
   @JoinTable(
      name = "user_authority",
      joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
   private Set<AuthorityEntity> authorities;*/

}
