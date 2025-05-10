package com.fifo.ticketing.domain.performance.mapper;

import com.fifo.ticketing.domain.performance.dto.PerformanceRequestDto;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.entity.Place;

public class PerformanceMapper {

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
    }
}
