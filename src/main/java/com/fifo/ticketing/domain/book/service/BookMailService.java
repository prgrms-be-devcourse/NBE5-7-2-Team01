package com.fifo.ticketing.domain.book.service;

import com.fifo.ticketing.domain.book.dto.BookMailSendDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class BookMailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendBookCompleteMail(BookMailSendDto bookMailSendDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(bookMailSendDto.getEmailAddr());
            helper.setSubject(bookMailSendDto.getTitle());

            Context context = new Context();
            context.setVariable("book", bookMailSendDto);

            String htmlContent = templateEngine.process("book/complete_mail", context);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 실패", e);

        }
    }

}
