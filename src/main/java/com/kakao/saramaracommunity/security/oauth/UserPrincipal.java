/*
package com.kakao.saramaracommunity.security.oauth;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UserPrincipal implements OAuth2User, UserDetails {

	private long id;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	@Setter
	private Map<String, Object> attributes;

	public static UserPrincipal create(User user) {
		List<GrantedAuthority> authorities =
			Collections.singletonList(new SimpleGrantedAuthority("" + RO));
		return new UserPrincipal(
			user.getUsername(),
			user.(),
			user.getPassword(),
			authorities,
			null
		);
	}

	public static UserPrincipal create(User user, Map<String, Object> attributes) {
		UserPrincipal userPrincipal = UserPrincipal.create(user);
		userPrincipal.setAttributes(attributes);
		return userPrincipal;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return String.valueOf(id);
	}

	@Override
	public String getUsername() {
		return email;
	}

}*/
