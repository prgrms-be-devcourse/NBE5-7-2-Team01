package com.fifo.ticketing.domain.performance.mapper;

import com.fifo.ticketing.domain.performance.dto.PerformanceResponseDto;
import com.fifo.ticketing.domain.performance.entity.Performance;
import org.springframework.data.domain.Page;

public class PerformanceMapper {

    private PerformanceMapper() {
    }

    private static PerformanceResponseDto toPerformanceResponseDto(Performance performance) {
        return PerformanceResponseDto.builder()
            .encodedFileName(performance.getFile().getFileName())
            .title(performance.getTitle())
            .category(performance.getCategory().name())
            .place(performance.getPlace().getName())
            .startTime(performance.getStartTime())
            .endTime(performance.getEndTime())
            .reservationStartTime(performance.getReservationStartTime())
            .performanceStatus(performance.isPerformanceStatus())
            .build();
    }

    public static Page<PerformanceResponseDto> toPagePerformanceResponseDto(
        Page<Performance> performances) {
        return performances.map(PerformanceMapper::toPerformanceResponseDto);
    }
}
