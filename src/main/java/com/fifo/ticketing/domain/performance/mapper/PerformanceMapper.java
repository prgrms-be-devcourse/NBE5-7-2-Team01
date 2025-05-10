package com.fifo.ticketing.domain.performance.mapper;

import com.fifo.ticketing.domain.performance.dto.PerformanceResponseDto;
import com.fifo.ticketing.domain.performance.dto.PerformanceRequestDto;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.entity.Place;
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
  
    public static Performance toEntity(PerformanceRequestDto dto, Place place) {
        return Performance.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .place(place)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .category(dto.getCategory())
                .performanceStatus(dto.isPerformanceStatus())
                .reservationStartTime(dto.getReservationStartTime())
                .build();

    public static Page<PerformanceResponseDto> toPagePerformanceResponseDto(
        Page<Performance> performances) {
        return performances.map(PerformanceMapper::toPerformanceResponseDto);
    }
}
