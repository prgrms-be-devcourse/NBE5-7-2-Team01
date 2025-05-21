package com.fifo.ticketing.global.event;

import com.fifo.ticketing.domain.like.dto.ReservationStartMailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class ReservationEvent {
    private final ReservationStartMailDto dto;

}
