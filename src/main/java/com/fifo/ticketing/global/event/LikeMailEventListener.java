package com.fifo.ticketing.global.event;

import com.fifo.ticketing.domain.like.service.LikeMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
public class LikeMailEventListener {

    private final LikeMailService likeMailService;


    @Async("mailExecutor")
    @TransactionalEventListener
    public void HandleLikeMailEvent(LikeMailEvent event) {
        likeMailService.performanceStart(event.getUser() , event.getPerformance());
    }
}