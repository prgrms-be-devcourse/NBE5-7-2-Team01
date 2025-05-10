package com.fifo.ticketing.domain.performance.mapper;

import com.fifo.ticketing.domain.performance.dto.GradeResponseDto;
import com.fifo.ticketing.domain.performance.entity.Grade;

public class GradeMapper {

    public static GradeResponseDto toDto(Grade grade) {
        return GradeResponseDto.builder()
                .grade(grade.getGrade())
                .seatCount(grade.getSeatCount())
                .defaultPrice(grade.getDefaultPrice())
                .build();
    }
}
