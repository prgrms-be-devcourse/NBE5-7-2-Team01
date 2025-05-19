package com.fifo.ticketing.domain.performance.service;

import com.fifo.ticketing.domain.book.dto.BookMailSendDto;
import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class PerformanceMailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void performanceStart(BookMailSendDto bookMailSendDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(bookMailSendDto.getEmailAddr());
            helper.setFrom(fromAddress);
            helper.setSubject(bookMailSendDto.getTitle() + "의 공연이 취소되었습니다.");

            Context context = new Context();
            context.setVariable("mailDto", bookMailSendDto);

            String htmlContent = templateEngine.process("book/complete_mail", context);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new ErrorException(ErrorCode.FAIL_EMAIL_SEND);
        }
    }
}
