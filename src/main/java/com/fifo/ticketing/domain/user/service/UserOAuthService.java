package com.fifo.ticketing.domain.user.service;

import com.fifo.ticketing.domain.user.dto.oauth.UserOAuthDetails;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserOAuthService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        UserOAuthDetails memberOAuthDetails = UserDetailsFactory.memberFormDetails(provider,
            oAuth2User);
        Optional<User> userOptional = userRepository.findByEmail(memberOAuthDetails.getEmail());
        User user = userOptional.orElseGet(
            () -> {
                User saved = User.builder()
                    .email(memberOAuthDetails.getEmail())
                    .username(memberOAuthDetails.getName())
                    .provider(provider)
                    .build();
                return userRepository.save(saved);
            }
        );
        if (user.getProvider() == null || !user.getProvider().equals(provider)) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_provider"),
                "이미 가입된 이메일입니다. 일반 로그인 방식으로 로그인 해주세요.");
        }
        return memberOAuthDetails.setRole(user.getRole());
    }
}
