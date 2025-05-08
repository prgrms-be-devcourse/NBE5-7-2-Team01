package com.fifo.ticketing.global.exception;

import static com.fifo.ticketing.global.exception.ErrorStatus.NOT_FOUND;
import static com.fifo.ticketing.global.exception.ErrorStatus.SEAT_BOOKING_FAILED;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND_MEMBER("AUTH-001", "존재하지 않는 회원입니다.", NOT_FOUND),
    SEAT_ALREADY_BOOKED("임시","해당 좌석은 이미 예약되었습니다.",SEAT_BOOKING_FAILED);

    private final String code;
    private final String message;
    private final ErrorStatus errorStatus;
}
