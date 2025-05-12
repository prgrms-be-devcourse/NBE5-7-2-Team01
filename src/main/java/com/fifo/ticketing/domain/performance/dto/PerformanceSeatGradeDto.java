package com.fifo.ticketing.domain.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PerformanceSeatGradeDto {

    private String grade;
    private int seatCount;
    private int defaultPrice;
}
