package com.fifo.ticketing.domain.like.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoPayedMailDto {
    private String email;
    private String username;
    private String performanceTitle;
    private int availableSeats;
}