package com.fifo.ticketing.global.exception;

import static com.fifo.ticketing.global.exception.ErrorStatus.ALREADY_EXISTS;
import static com.fifo.ticketing.global.exception.ErrorStatus.CONFLICT;
import static com.fifo.ticketing.global.exception.ErrorStatus.INTERNAL_SERVER_ERROR;
import static com.fifo.ticketing.global.exception.ErrorStatus.NOT_FOUND;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND_MEMBER("AUTH-001", "존재하지 않는 회원입니다.", NOT_FOUND),
    NOT_FOUND_PERFORMANCES("PERFORMANCE-001", "예매 가능한 공연이 존재하지 않습니다.", NOT_FOUND),
    SEAT_ALREADY_BOOKED("SEAT-001", "해당 좌석은 이미 예약되었습니다.", CONFLICT),
    FILE_UPLOAD_FAILED("FILE-001", "파일 업로드에 실패하였습니다.", INTERNAL_SERVER_ERROR),
    NOT_FOUND_PLACES("PLACE-001", "존재하지 않는 공연장입니다.", NOT_FOUND),
    NOT_FOUND_GRADE("GRADE-001", "공연장에 매핑된 좌석 등급이 없습니다.", NOT_FOUND),
    SEAT_CREATE_FAILED("SEAT-002", "좌석 등록에 실패하였습니다.", INTERNAL_SERVER_ERROR),
    EMAIL_ALREADY_EXISTS("EMAIL-001", "이미 사용하는 이메일입니다.", ALREADY_EXISTS),
    NOT_FOUND_AUTH("AUTH-002", "잘못된 인증번호 입니다.", NOT_FOUND),
    NOT_FOUND_PROVIDER("PROVIDER-001", "지원하지 않는 플랫폼 서비스입니다.", NOT_FOUND);

    private final String code;
    private final String message;
    private final ErrorStatus errorStatus;
}
