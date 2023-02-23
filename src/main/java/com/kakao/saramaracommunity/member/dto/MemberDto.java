package com.kakao.saramaracommunity.member.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Type;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// 회원가입, 회원정보 조회, 회원 정보 수정 시 사용되는 DTO
@Getter
@Builder
@AllArgsConstructor
public class MemberDto {

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

	public static MemberDto from(Member memberEntity){
		if(memberEntity == null) return null;

		return MemberDto.builder()
			.email(memberEntity.getEmail())
			.nickname(memberEntity.getNickname())
			.picture(memberEntity.getPicture())
			.type(memberEntity.getType())
			.build();
	}
}
