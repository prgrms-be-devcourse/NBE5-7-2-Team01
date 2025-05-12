package com.fifo.ticketing.domain.seat.mapper;

import com.fifo.ticketing.domain.book.dto.BookSeatViewDto;
import com.fifo.ticketing.domain.performance.entity.Grade;
import com.fifo.ticketing.domain.seat.entity.Seat;

public class SeatMapper {

    public static BookSeatViewDto toBookSeatViewDto(Seat seat) {
        Grade grade = seat.getGrade();

        return BookSeatViewDto.builder()
            .seatId(seat.getId())
            .seatNumber(seat.getSeatNumber())
            .grade(grade.getGrade())
            .price(seat.getPrice())
            .seatStatus(seat.getSeatStatus())
            .build();
    }
}
