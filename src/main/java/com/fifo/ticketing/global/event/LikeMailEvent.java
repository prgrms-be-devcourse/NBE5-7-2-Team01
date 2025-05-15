package com.fifo.ticketing.global.event;

import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class LikeMailEvent {
    private final User user;
    private final Performance performance;

}
