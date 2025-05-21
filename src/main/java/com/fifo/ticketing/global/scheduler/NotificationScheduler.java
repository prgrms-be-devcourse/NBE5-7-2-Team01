package com.fifo.ticketing.global.scheduler;


import com.fifo.ticketing.domain.like.service.LikeMailNotificationService;
import com.fifo.ticketing.domain.performance.service.PerformanceReservationOpenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final LikeMailNotificationService likeMailNotificationService;
    private final PerformanceReservationOpenService performanceReservationOpenService;

    @Scheduled(cron = "0 30 12 * * *")
    public void likeMailNotification() {
        likeMailNotificationService.sendTimeNotification();
    }


    @Scheduled(cron = "0 0 2 * * *")
    public void NoPayedNotification() {
        likeMailNotificationService.sendNoPayedNotification();
    }

    @Scheduled(cron = "0 0 13 * * *")
    public void updatePerformanceStatus() {
        performanceReservationOpenService.updateStatusIfReservationStart();
    }
}
