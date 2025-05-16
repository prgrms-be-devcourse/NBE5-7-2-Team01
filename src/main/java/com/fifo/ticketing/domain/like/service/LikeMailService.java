package com.fifo.ticketing.domain.like.service;

import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import com.fifo.ticketing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeMailService {
    private final JavaMailSender javaMailSender;
    private final SeatRepository seatRepository;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Async("mailExecutor")
    public void performanceStart(User user, Performance performance){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromAddress);
        //이메일 제목
        message.setSubject(performance.getTitle()+"의 티켓팅 시작이 30분 남았습니다.");
        //이메일 받을 대상
        message.setTo(user.getEmail());
        //이메일 내용
        message.setText(user.getUsername()+"님 좋아요를 눌러주신 공연 "+ performance.getTitle()+"의 티켓팅 시작이 30분 남았습니다."+
        "감사합니다");

        javaMailSender.send(message);

    }

    @Async("mailExecutor")
    public void NoPayedPerformance(User user, Performance performance){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromAddress);
        //이메일 제목
        message.setSubject(performance.getTitle()+"의 티켓팅 시작이 1시간 지났습니다.");
        //이메일 받을 대상
        message.setTo(user.getEmail());
        //이메일 내용
        message.setText(user.getUsername()+"님 좋아요를 눌러주신 공연 "+ performance.getTitle()+"의 좌석이" +
            seatRepository.countAvailableSeatsByPerformanceId(performance.getId()) +"개 남았습니다."+
            "감사합니다");

        javaMailSender.send(message);

    }

}
