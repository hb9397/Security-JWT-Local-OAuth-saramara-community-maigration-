package com.kakao.saramaracommunity.member.dto;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class SecurityMemberDto extends User implements OAuth2User {

	//private Long memberId;
	private String email;
	private String password;
	private String nickname;
	private Type type;
	private String profileImage;
	private String refreshToken;

	private Map<String, Object> attattributes;
	private Collection<? extends GrantedAuthority> authorities;


	public SecurityMemberDto(String email, String password, String nickname, Type type, String profileImage,String refreshToken,
		Collection<? extends GrantedAuthority> authorities
	) {
		super(email, password, authorities);

		//this.memberId = memberId;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.type = type;
		this.profileImage = profileImage;
		this.refreshToken = refreshToken;
		this.authorities = authorities;
	}

	public static SecurityMemberDto from(UserEntity userEntity){
		if(userEntity == null) { return null; }

		return SecurityMemberDto.builder()
			.email(userEntity.getEmail())
			.password(null)
			.nickname(userEntity.getNickname())
			.type(userEntity.getType())
			.profileImage(userEntity.getProfileImage())
			.refreshToken(userEntity.getRefreshToken())
			.authorities(userEntity.getRole().stream().map(userRole ->
				new SimpleGrantedAuthority("ROLE_" + userRole.name())
			).collect(Collectors.toList()))
			.build();
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}
}
