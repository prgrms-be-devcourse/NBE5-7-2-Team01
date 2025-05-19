package com.fifo.ticketing.domain.like.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationStartMailDto {

    private String email;
    private String username;
    private String performanceTitle;
    private LocalDateTime reservationStartTime;
}
