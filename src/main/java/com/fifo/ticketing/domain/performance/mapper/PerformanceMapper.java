package com.fifo.ticketing.domain.performance.mapper;

import com.fifo.ticketing.domain.performance.dto.PerformanceDetailResponse;
import com.fifo.ticketing.domain.performance.dto.PerformanceRequestDto;
import com.fifo.ticketing.domain.performance.dto.PerformanceResponseDto;
import com.fifo.ticketing.domain.performance.dto.PerformanceSeatGradeDto;
import com.fifo.ticketing.domain.performance.entity.Grade;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.entity.Place;
import java.util.List;
import org.springframework.data.domain.Page;

public class PerformanceMapper {

    public static PerformanceDetailResponse toDetailResponseDto(Performance performance,
        List<PerformanceSeatGradeDto> seatGrades) {
        return PerformanceDetailResponse.builder().performanceId(performance.getId())
            .title(performance.getTitle()).description(performance.getDescription())
            .category(performance.getCategory().name()).startTime(performance.getStartTime())
            .encodedFileName(performance.getFile().getEncodedFileName())
            .endTime(performance.getEndTime()).placeName(performance.getPlace().getName())
            .address(performance.getPlace().getAddress())
            .totalSeats(performance.getPlace().getTotalSeats()).seatGrades(seatGrades).build();

    }

    public static PerformanceSeatGradeDto toSeatGradeDto(Grade grade) {
        return PerformanceSeatGradeDto.builder().grade(grade.getGrade())
            .defaultPrice(grade.getDefaultPrice()).seatCount(grade.getSeatCount()).build();
    }

    private PerformanceMapper() {
    }

    private static PerformanceResponseDto toPerformanceResponseDto(Performance performance) {
        return PerformanceResponseDto.builder()
            .id(performance.getId())
            .encodedFileName(performance.getFile().getEncodedFileName())
            .title(performance.getTitle())
            .category(performance.getCategory().name())
            .place(performance.getPlace().getName())
            .startTime(performance.getStartTime())
            .endTime(performance.getEndTime())
            .reservationStartTime(performance.getReservationStartTime())
            .performanceStatus(performance.isPerformanceStatus()).build();
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
    }

    public static Page<PerformanceResponseDto> toPagePerformanceResponseDto(
        Page<Performance> performances) {
        return performances.map(PerformanceMapper::toPerformanceResponseDto);
    }
}
