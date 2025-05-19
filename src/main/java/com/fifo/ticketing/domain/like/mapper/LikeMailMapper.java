package com.fifo.ticketing.domain.like.mapper;

import com.fifo.ticketing.domain.like.dto.NoPayedMailDto;
import com.fifo.ticketing.domain.like.dto.ReservationStartMailDto;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.user.entity.User;

public class LikeMailMapper {
    public static ReservationStartMailDto toReservationStartMailDto(User user, Performance performance) {
        return ReservationStartMailDto.builder()
            .email(user.getEmail())
            .username(user.getUsername())
            .performanceTitle(performance.getTitle())
            .reservationStartTime(performance.getReservationStartTime())
            .build();
    }

    public static NoPayedMailDto toNoPayedMailDto(User user, Performance performance, int availableSeats) {
        return NoPayedMailDto.builder()
            .email(user.getEmail())
            .username(user.getUsername())
            .performanceTitle(performance.getTitle())
            .availableSeats(availableSeats)
            .build();
    }

}
