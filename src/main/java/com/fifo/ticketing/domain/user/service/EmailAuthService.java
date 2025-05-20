package com.fifo.ticketing.domain.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import jakarta.servlet.http.HttpSession;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final RedisService redisService;
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String setForm;

    public void sendEmail(String toEmail) throws MessagingException {
        String authCode = createCode();
        String setKey = "EAC:" + toEmail;
        redisService.setValuesWithTimeout(setKey, authCode, 5 * 60 * 1000);
        MimeMessage emailForm = createEmailForm(toEmail, authCode);
        emailSender.send(emailForm);
    }

    public String createCode() {

        Random random = new Random();
        StringBuilder authCode = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            authCode.append(random.nextInt(9));
        }

        return authCode.toString();
    }

    public MimeMessage createEmailForm(String email, String authCode) throws MessagingException {
        String title = "Ticketing 회원가입 인증 번호";
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(RecipientType.TO, email);
        message.setSubject(title);
        message.setFrom(setForm);
        message.setText(setContext(authCode), "utf-8", "html");
        return message;
    }

    public String setContext(String authCode) {
        Context context = new Context();
        context.setVariable("code", authCode);
        return templateEngine.process("mail", context);
    }

    public boolean checkAuthCode(String toEmail, String authCode, HttpSession session) {
        String key = "EAC:" + toEmail;
        String findAuthCode = redisService.getValues(key);

        if (findAuthCode == null) {
            return false;
        }

        if (findAuthCode.equals(authCode)) {
            redisService.deleteValues(key);
            session.setAttribute("emailVerified", toEmail);
            return true;
        }
        return false;
    }

}
