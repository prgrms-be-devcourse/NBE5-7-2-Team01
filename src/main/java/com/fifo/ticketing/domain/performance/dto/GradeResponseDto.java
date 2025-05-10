package com.fifo.ticketing.domain.performance.dto;

import com.fifo.ticketing.domain.performance.entity.Grade;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GradeResponseDto {
    String grade;
    Integer seatCount;
    Integer defaultPrice;
}
