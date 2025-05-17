package com.fifo.ticketing.domain.user.service.handler;

import com.fifo.ticketing.domain.user.dto.SessionUser;
import com.fifo.ticketing.domain.user.dto.oauth.UserOAuthDetails;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.domain.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        UserOAuthDetails userDetails = (UserOAuthDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow();
        request.getSession()
            .setAttribute("loginUser", new SessionUser(user.getId(), user.getUsername()));

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            response.sendRedirect(targetUrl);
        } else {
            response.sendRedirect("/");
        }
    }
}
