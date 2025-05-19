package com.fifo.ticketing.domain.like.service;

import com.fifo.ticketing.domain.like.dto.NoPayedMailDto;
import com.fifo.ticketing.domain.like.dto.ReservationStartMailDto;
import com.fifo.ticketing.domain.like.mapper.LikeMailMapper;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import com.fifo.ticketing.domain.user.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class LikeMailService {
    private final JavaMailSender javaMailSender;
    private final SeatRepository seatRepository;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Async("mailExecutor")
    public void reservationStart( ReservationStartMailDto dto ) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(dto.getEmail());
            helper.setFrom(fromAddress);
            helper.setSubject("[알림] " + dto.getPerformanceTitle() + " 예매 시작 30분 전입니다.");

            Context context = new Context();
            context.setVariable("mailDto", dto);

            String htmlContent = templateEngine.process("mail/reservation_start_notice", context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("예매 시작 알림 메일 전송 실패", e);
        }
    }

    @Async("mailExecutor")
    public void NoPayedPerformance(NoPayedMailDto dto) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(dto.getEmail());
            helper.setFrom(fromAddress);
            helper.setSubject("[미예매 알림] " + dto.getPerformanceTitle() + " 예매를 잊으셨나요?");

            Context context = new Context();
            context.setVariable("mailDto", dto);

            String htmlContent = templateEngine.process("mail/no_payed_notice", context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("미예매자 알림 메일 전송 실패", e);
        }
    }

}
