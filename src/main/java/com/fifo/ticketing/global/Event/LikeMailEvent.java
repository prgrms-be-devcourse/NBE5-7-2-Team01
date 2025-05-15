package com.fifo.ticketing.global.Event;

import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.user.entity.User;
import jakarta.mail.event.MailEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class LikeMailEvent {
    private final User user;
    private final Performance performance;
    private final MailType mailType;

}
