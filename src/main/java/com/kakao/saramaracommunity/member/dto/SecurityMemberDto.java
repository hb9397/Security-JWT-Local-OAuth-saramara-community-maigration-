package com.kakao.saramaracommunity.member.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


// 인증, 인가에 대한 사용자의 정보를 반환할 때 사용하는 DTO
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecurityMemberDto {

	//private Long memberId;
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

	private String picture;

	private Type type;

	private Set<Role> role;

}
