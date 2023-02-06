package com.kakao.saramaracommunity.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

// Lombok 어노테이션
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
   // Validation 어노테이션
   @NotNull
   @Size(min = 3, max = 50)
   private String username;

   @NotNull
   @Size(min = 3, max = 100)
   private String password;
}
