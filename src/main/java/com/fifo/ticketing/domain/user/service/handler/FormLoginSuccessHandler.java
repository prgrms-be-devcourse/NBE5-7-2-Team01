package com.fifo.ticketing.domain.user.service.handler;

import com.fifo.ticketing.domain.user.dto.SessionUser;
import com.fifo.ticketing.domain.user.dto.form.UserFormDetails;
import com.fifo.ticketing.domain.user.entity.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        UserFormDetails userDetails = (UserFormDetails) authentication.getPrincipal();

        HttpSession session = request.getSession();
        session.setAttribute("loginUser",
            new SessionUser(userDetails.getId(), userDetails.getName(), userDetails.getRole()));

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            response.sendRedirect(targetUrl);
        } else if (userDetails.getRole().equals(Role.ADMIN)) {
            response.sendRedirect("/admin/menu");
        } else {
            response.sendRedirect("/performances");
        }
    }
}
