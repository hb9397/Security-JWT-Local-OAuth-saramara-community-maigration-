package com.kakao.saramaracommunity.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakao.saramaracommunity.member.entity.UserEntity;
import com.kakao.saramaracommunity.member.entity.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

   @NotNull
   @Size(max = 50)
   private String email;

   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   @NotNull
   @Size(min = 3, max = 100)
   private String password;

   @NotNull
   @Size(min = 3, max = 50)
   private String nickname;

   private String profileImage;

   private Set<UserRole> userRole;

   public static SignUpDto from(UserEntity user) {
      if(user == null) return null;

      return SignUpDto.builder()
              .email(user.getEmail())
              .nickname(user.getNickname())
              .userRole(user.getRole().stream()
                      .map(userRole -> UserRole.USER)
                      .collect(Collectors.toSet()))
              .build();
   }
}