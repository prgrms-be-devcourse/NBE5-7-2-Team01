package com.fifo.ticketing.domain.user.service;

import com.fifo.ticketing.domain.user.dto.oauth.UserOAuthDetails;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import java.util.Map;
import org.springframework.security.oauth2.core.user.OAuth2User;

@SuppressWarnings("unchecked")
public class UserDetailsFactory {

    public static UserOAuthDetails memberFormDetails(String provider, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        switch (provider.toUpperCase()) {
            case "GOOGLE" -> {
                return UserOAuthDetails.builder()
                    .name(attributes.get("name").toString())
                    .email(attributes.get("email").toString())
                    .attributes(attributes)
                    .build();
            }
            case "NAVER" -> {
                Map<String, Object> properties = (Map<String, Object>) attributes.get("response");

                return UserOAuthDetails.builder()
                    .name(properties.get("name").toString())
                    .email(properties.get("id") + "@naver.com")
                    .attributes(properties)
                    .build();
            }
            case "KAKAO" -> {
                Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
                return UserOAuthDetails.builder()
                    .name(properties.get("nickname").toString())
                    .email(attributes.get("id") + "@kakao.com")
                    .attributes(properties)
                    .build();
            }
            default -> {
                throw new ErrorException(ErrorCode.NOT_FOUND_PROVIDER);
            }
        }
    }
}
