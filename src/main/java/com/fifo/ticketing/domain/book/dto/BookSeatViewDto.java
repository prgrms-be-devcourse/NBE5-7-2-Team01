package com.fifo.ticketing.domain.book.dto;

import com.fifo.ticketing.domain.seat.entity.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookSeatViewDto {

    private Long seatId;
    private String seatNumber;
    private String grade;
    private int price;
    private SeatStatus seatStatus;
}