package com.fifo.ticketing.global.event;

import com.fifo.ticketing.domain.like.dto.NoPayedMailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoPayMailEvent {
    private final NoPayedMailDto dto;
}
