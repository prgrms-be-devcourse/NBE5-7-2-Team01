package com.fifo.ticketing.global.scheduler;


import com.fifo.ticketing.domain.like.service.LikeMailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final LikeMailNotificationService likeMailNotificationService;

    @Scheduled(cron = "0 30 * * * *") // 매 시 (30분)에 실행
    public void likeMailNotification(){
        likeMailNotificationService.sendTimeNotification();

    }

    @Scheduled(cron = "0 0 * * * *")
    public void NoPayedNotification(){
        likeMailNotificationService.sendNoPayedNotification();

    }



}
