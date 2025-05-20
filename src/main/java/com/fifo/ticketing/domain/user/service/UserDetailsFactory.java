package com.fifo.ticketing.domain.user.service;

import com.fifo.ticketing.domain.user.dto.oauth.UserOAuthDetails;
import com.fifo.ticketing.domain.user.entity.OAuthProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserDetailsFactory {

    public static UserOAuthDetails memberFormDetails(String provider, OAuth2User oAuth2User) {
        return OAuthProvider.from(provider).extract(oAuth2User);
    }
}
