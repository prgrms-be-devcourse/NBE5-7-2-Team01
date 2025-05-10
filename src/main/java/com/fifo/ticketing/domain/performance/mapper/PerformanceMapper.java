package com.fifo.ticketing.domain.performance.mapper;

import com.fifo.ticketing.domain.performance.dto.PerformanceDetailResponse;
import com.fifo.ticketing.domain.performance.dto.PerformanceResponseDto;
import com.fifo.ticketing.domain.performance.dto.PerformanceSeatGradeDto;
import com.fifo.ticketing.domain.performance.entity.Grade;
import com.fifo.ticketing.domain.performance.entity.Performance;
import org.springframework.data.domain.Page;

import java.util.List;

public class PerformanceMapper {

    public static PerformanceDetailResponse toDetailResponseDto(Performance performance,
        List<PerformanceSeatGradeDto> seatGrades) {
        return PerformanceDetailResponse.builder().performanceId(performance.getId())
            .title(performance.getTitle()).description(performance.getDescription())
            .category(performance.getCategory().name()).startTime(performance.getStartTime())
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
        return PerformanceResponseDto.builder().encodedFileName(performance.getFile().getFileName())
            .title(performance.getTitle()).category(performance.getCategory().name())
            .place(performance.getPlace().getName()).startTime(performance.getStartTime())
            .endTime(performance.getEndTime())
            .reservationStartTime(performance.getReservationStartTime())
            .performanceStatus(performance.isPerformanceStatus()).build();
    }

    public static Page<PerformanceResponseDto> toPagePerformanceResponseDto(
        Page<Performance> performances) {
        return performances.map(PerformanceMapper::toPerformanceResponseDto);
    }
}
