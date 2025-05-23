package com.fifo.ticketing.domain.performance.mapper;

import com.fifo.ticketing.domain.performance.dto.AdminPerformanceDetailResponse;
import com.fifo.ticketing.domain.performance.dto.AdminPerformanceResponseDto;
import com.fifo.ticketing.domain.performance.dto.LikedPerformanceDto;
import com.fifo.ticketing.domain.performance.dto.PerformanceDetailResponse;
import com.fifo.ticketing.domain.performance.dto.PerformanceRequestDto;
import com.fifo.ticketing.domain.performance.dto.PerformanceResponseDto;
import com.fifo.ticketing.domain.performance.dto.PerformanceSeatGradeDto;
import com.fifo.ticketing.domain.performance.entity.Grade;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.entity.Place;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public class PerformanceMapper {

    private PerformanceMapper() {
    }

    public static PerformanceDetailResponse toDetailResponseDto(Performance performance,
        List<PerformanceSeatGradeDto> seatGrades, String urlPrefix) {
        return PerformanceDetailResponse.builder()
            .performanceId(performance.getId())
            .title(performance.getTitle())
            .description(performance.getDescription())
            .category(performance.getCategory().name())
            .startTime(performance.getStartTime())
            .encodedFileName(performance.getFile().getEncodedFileName())
            .endTime(performance.getEndTime())
            .reservationStartTime(performance.getReservationStartTime())
            .placeName(performance.getPlace().getName())
            .address(performance.getPlace().getAddress())
            .performanceStatus(performance.isPerformanceStatus())
            .totalSeats(performance.getPlace().getTotalSeats())
            .seatGrades(seatGrades)
            .urlPrefix(urlPrefix)
            .build();
    }

    public static AdminPerformanceDetailResponse toAdminDetailResponseDto(Performance performance,
        List<PerformanceSeatGradeDto> seatGrades, String urlPrefix) {
        return AdminPerformanceDetailResponse.builder().performanceId(performance.getId())
            .title(performance.getTitle())
            .description(performance.getDescription())
            .category(performance.getCategory().name())
            .startTime(performance.getStartTime())
            .encodedFileName(performance.getFile().getEncodedFileName())
            .endTime(performance.getEndTime())
            .performanceStatus(performance.isPerformanceStatus())
            .deletedFlag(performance.isDeletedFlag())
            .placeName(performance.getPlace().getName())
            .address(performance.getPlace().getAddress())
            .totalSeats(performance.getPlace().getTotalSeats())
            .seatGrades(seatGrades)
            .urlPrefix(urlPrefix)
            .build();
    }

    public static PerformanceSeatGradeDto toSeatGradeDto(Grade grade) {
        return PerformanceSeatGradeDto.builder().grade(grade.getGrade())
            .defaultPrice(grade.getDefaultPrice()).seatCount(grade.getSeatCount()).build();
    }

    public static PerformanceResponseDto toPerformanceResponseDto(Performance performance,
        String urlPrefix) {
        return PerformanceResponseDto.builder()
            .id(performance.getId())
            .encodedFileName(performance.getFile().getEncodedFileName())
            .title(performance.getTitle())
            .category(performance.getCategory().name())
            .place(performance.getPlace().getName())
            .startTime(performance.getStartTime())
            .endTime(performance.getEndTime())
            .reservationStartTime(performance.getReservationStartTime())
            .performanceStatus(performance.isPerformanceStatus())
            .urlPrefix(urlPrefix)
            .build();
    }

    public static Performance toEntity(PerformanceRequestDto dto, Place place) {
        LocalDate dateOnly = dto.getReservationStartTime().toLocalDate();
        LocalDateTime fixedReservationStartTime = dateOnly.atTime(13, 0);
        return Performance.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .place(place)
            .startTime(dto.getStartTime())
            .endTime(dto.getEndTime())
            .category(dto.getCategory())
            .performanceStatus(dto.isPerformanceStatus())
            .reservationStartTime(fixedReservationStartTime)
            .build();
    }

    public static Page<PerformanceResponseDto> toPagePerformanceResponseDto(
        Page<Performance> performances, String urlPrefix) {
        return performances.map(
            performance -> PerformanceMapper.toPerformanceResponseDto(performance, urlPrefix));
    }

    public static AdminPerformanceResponseDto toAdminPerformanceResponseDto(Performance performance,
        String urlPrefix) {
        return AdminPerformanceResponseDto.builder()
            .id(performance.getId())
            .encodedFileName(performance.getFile().getEncodedFileName())
            .title(performance.getTitle())
            .description(performance.getDescription())
            .category(performance.getCategory().name())
            .place(performance.getPlace().getName())
            .startTime(performance.getStartTime())
            .endTime(performance.getEndTime())
            .reservationStartTime(performance.getReservationStartTime())
            .performanceStatus(performance.isPerformanceStatus())
            .urlPrefix(urlPrefix)
            .id(performance.getId())
            .build();
    }

    public static LikedPerformanceDto toLikedPerformanceDto(Performance performance,
        String prefix) {
        return LikedPerformanceDto.builder()
            .id(performance.getId())
            .title(performance.getTitle())
            .encodedFileName(performance.getFile().getEncodedFileName())
            .startTime(performance.getStartTime())
            .endTime(performance.getEndTime())
            .placeName(performance.getPlace().getName())
            .urlPrefix(prefix)
            .build();
    }

    public static Page<AdminPerformanceResponseDto> toPageAdminPerformanceResponseDto(
        Page<Performance> performances, String urlPrefix) {
        return performances.map(
            performance -> PerformanceMapper.toAdminPerformanceResponseDto(performance, urlPrefix));
    }

    public static Page<LikedPerformanceDto> toPageLikedPerformanceDto(
        Page<Performance> performances, String urlPrefix) {
        return performances.map(
            performance -> PerformanceMapper.toLikedPerformanceDto(performance, urlPrefix)
        );
    }
}
