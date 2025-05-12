package com.fifo.ticketing.domain.like.service;

import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeMailService {
    private final JavaMailSender javaMailSender;

    public void performanceStart(User user, Performance performance){
        SimpleMailMessage message = new SimpleMailMessage();

        //이메일 제목
        message.setSubject(performance.getTitle()+"의 티켓팅 시작이 30분 남았습니다.");
        //이메일 받을 대상
        message.setTo(user.getEmail());
        //이메일 내용
        message.setText(user.getUsername()+"님 좋아요를 눌러주신 공연 "+ performance.getTitle()+"의 티켓팅 시작이 30분 남았습니다."+
        "감사합니다");

        javaMailSender.send(message);

    }

}
