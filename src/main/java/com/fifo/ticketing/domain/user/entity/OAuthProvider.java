package com.fifo.ticketing.domain.user.entity;

import com.fifo.ticketing.domain.user.dto.oauth.UserOAuthDetails;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import java.util.Map;
import org.springframework.security.oauth2.core.user.OAuth2User;

@SuppressWarnings("unchecked")
public enum OAuthProvider {

    GOOGLE {
        @Override
        public UserOAuthDetails extract(OAuth2User oAuth2User) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            return UserOAuthDetails.builder()
                .name(attributes.get("name").toString())
                .email(attributes.get("email").toString())
                .attributes(attributes)
                .build();
        }
    },
    NAVER {
        @Override
        public UserOAuthDetails extract(OAuth2User oAuth2User) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> properties = (Map<String, Object>) attributes.get("response");
            return UserOAuthDetails.builder()
                .name(properties.get("name").toString())
                .email(properties.get("id") + "@naver.com")
                .attributes(properties)
                .build();
        }
    },
    KAKAO {
        @Override
        public UserOAuthDetails extract(OAuth2User oAuth2User) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            return UserOAuthDetails.builder()
                .name(properties.get("nickname").toString())
                .email(attributes.get("id") + "@kakao.com")
                .attributes(properties)
                .build();
        }
    };

    public static OAuthProvider from(String provider) {
        try {
            return OAuthProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ErrorException(ErrorCode.NOT_FOUND_PROVIDER);
        }
    }

    public abstract UserOAuthDetails extract(OAuth2User oAuth2User);
}
