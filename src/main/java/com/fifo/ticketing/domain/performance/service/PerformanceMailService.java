package com.fifo.ticketing.domain.performance.service;

import com.fifo.ticketing.domain.book.entity.Book;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceMailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void performanceStart(Book book) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromAddress);
        //이메일 제목
        message.setSubject(book.getPerformance().getTitle() + "의 공연이 취소되었습니다.");
        //이메일 받을 대상
        message.setTo(book.getUser().getEmail());
        //이메일 내용

        message.setText(book.getUser().getUsername() + "님께서 예매해주신 공연이 취소되었습니다."
        + "정말로 죄송합니다." + " 해당 청구 금액에 대해서는 환급 예정입니다.");
        javaMailSender.send(message);
    }
}
