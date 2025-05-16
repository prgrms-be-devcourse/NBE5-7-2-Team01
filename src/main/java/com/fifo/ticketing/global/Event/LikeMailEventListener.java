package com.fifo.ticketing.global.Event;

import com.fifo.ticketing.domain.like.service.LikeMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
public class LikeMailEventListener {

    private final LikeMailService likeMailService;


    @Async("mailExecutor")
    @EventListener
    public void handleLikeMailEvent(LikeMailEvent event) {
        switch (event.getMailType()){
            case NO_PAYED -> likeMailService.NoPayedPerformance(event.getUser() , event.getPerformance());
            case RESERVATION_NOTICE -> likeMailService.performanceStart(event.getUser() , event.getPerformance());
        }

    }



}