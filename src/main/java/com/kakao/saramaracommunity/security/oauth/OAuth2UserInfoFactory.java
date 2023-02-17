package com.kakao.saramaracommunity.security.oauth;

import java.util.Map;

import com.kakao.saramaracommunity.member.entity.Type;

public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		if (registrationId.equalsIgnoreCase(Type.GOOGLE.name())) {
			return new GoogleOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase(Type.NAVER.name())) {
			return new NaverOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase(Type.KAKAO.name())) {
			return new KakaoOAuth2UserInfo(attributes);
		} else {
			throw new OAuth2AuthenticationProcessingException("Unsupported Login Type : " + registrationId);
		}
	}
}
