package com.fifo.ticketing.domain.performance.dto;

import com.fifo.ticketing.domain.performance.entity.Grade;

public record GradeResponseDto(
        String grade, Integer seatCount, Integer defaultPrice
) {
    public static GradeResponseDto from(Grade grade) {
        return new GradeResponseDto(grade.getGrade(), grade.getSeatCount(), grade.getDefaultPrice());
    }
}
