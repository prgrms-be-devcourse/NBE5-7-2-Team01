package com.fifo.ticketing.global.event;

import static com.fifo.ticketing.global.event.MailType.NO_PAYED;

import com.fifo.ticketing.domain.like.service.LikeMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LikeMailEventListener {

    private final LikeMailService likeMailService;


    @Async("mailExecutor")
    @EventListener
    public void handleLikeMailEvent(ReservationEvent event) {
        likeMailService.reservationStart(event.getDto());

    }

    @Async("mailExecutor")
    @EventListener
    public void handleLikeMailEvent(NoPayMailEvent event) {
        likeMailService.NoPayedPerformance(event.getDto());

    }


}