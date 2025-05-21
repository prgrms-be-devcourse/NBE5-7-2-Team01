package com.fifo.ticketing.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

class EmailAuthServiceTests {

    @InjectMocks
    private EmailAuthService emailAuthService;

    @Mock
    private RedisService redisService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("mail"), any(Context.class))).thenReturn(
            "<html><body>Code: 123456</body></html>");
    }

    @Test
    void testSendEmail() throws MessagingException {
        String toMail = "test@test.com";

        emailAuthService.sendEmail(toMail);

        verify(redisService, times(1)).setValuesWithTimeout(
            eq("EAC:" + toMail), anyString(), eq(5 * 60 * 1000L));
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testCorrectAuthCode() {
        String email = "test@test.com";
        String authCode = "123456";
        when(redisService.getValues("EAC:" + email)).thenReturn(authCode);

        boolean checked = emailAuthService.checkAuthCode(email, authCode, session);

        verify(redisService).deleteValues("EAC:" + email);
        verify(session).setAttribute("emailVerified", email);
        assertThat(checked).isTrue();
    }

    @Test
    void testIncorrectAuthCode() {
        String email = "test@test.com";
        String authCode = "123456";
        when(redisService.getValues("EAC:" + email)).thenReturn("654321");

        boolean checked = emailAuthService.checkAuthCode(email, authCode, session);

        verify(redisService, never()).deleteValues(any());
        verify(session, never()).setAttribute(any(), any());
        assertThat(checked).isFalse();
    }

}