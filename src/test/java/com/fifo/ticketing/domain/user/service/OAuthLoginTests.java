package com.fifo.ticketing.domain.user.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fifo.ticketing.domain.user.dto.SessionUser;
import com.fifo.ticketing.domain.user.dto.oauth.UserOAuthDetails;
import com.fifo.ticketing.domain.user.entity.Role;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.domain.user.repository.UserRepository;
import com.fifo.ticketing.domain.user.service.handler.OAuth2LoginFailureHandler;
import com.fifo.ticketing.domain.user.service.handler.OAuth2LoginSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("ci")
@ExtendWith(MockitoExtension.class)
class OAuthLoginTests {

    @Mock
    private HttpSession session;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthenticationException exception;

    @InjectMocks
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @InjectMocks
    private OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Test
    @DisplayName("기존에 있는 유저 로그인 성공 테스트")
    void oAuth2LoginSuccessHandler_test_success() throws ServletException, IOException {

        UserOAuthDetails userDetails = UserOAuthDetails.builder()
            .email("test@test.com")
            .name("테스트 유저")
            .attributes(Map.of(
                "email", "test@test.com",
                "name", "테스트 유저"))
            .role(Role.USER)
            .build();

        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
            userDetails,
            userDetails.getAuthorities(),
            "google"
        );

        given(userRepository.findByEmail("test@test.com")).willReturn(
            Optional.of(User.builder()
                .id(1L)
                .email("test@test.com")
                .username("테스트 유저")
                .provider("google")
                .build())
        );

        given(request.getSession()).willReturn(session);

        oAuth2LoginSuccessHandler.onAuthenticationSuccess(request, response, token);

        verify(session).setAttribute("loginUser", new SessionUser(1L, "테스트 유저", Role.USER));
        verify(response).sendRedirect("/");
    }

    @Test
    @DisplayName("로그인 실패 시 세션에 에러 메세지 저장, 로그인 페이지로 리다이렉트")
    void oAuth2LoginFailureHandler_test_failure() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(exception.getMessage()).thenReturn("소셜 로그인 실패");

        oAuth2LoginFailureHandler.onAuthenticationFailure(request, response, exception);

        verify(session).setAttribute("errormessage", "소셜 로그인 실패");
        verify(response).sendRedirect("/users/signin");

    }
}
