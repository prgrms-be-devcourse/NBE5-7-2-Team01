package com.fifo.ticketing.domain.performance.service;

import static com.fifo.ticketing.global.exception.ErrorCode.NOT_FOUND_PERFORMANCES;

import com.fifo.ticketing.domain.performance.dto.PerformanceResponseDto;
import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.mapper.PerformanceMapper;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.global.exception.ErrorException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public Page<PerformanceResponseDto> getPerformancesSortedByLatest(Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpcomingPerformancesOrderByReservationStartTime(
            LocalDateTime.now(), pageable);
        if (performances.isEmpty()) {
            throw new ErrorException(NOT_FOUND_PERFORMANCES);
        }
        return PerformanceMapper.toPagePerformanceResponseDto(performances);
    }

    @Transactional(readOnly = true)
    public Page<PerformanceResponseDto> getPerformancesSortedByLikes(Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpcomingPerformancesOrderByLikes(
            LocalDateTime.now(), pageable);
        if (performances.isEmpty()) {
            throw new ErrorException(NOT_FOUND_PERFORMANCES);
        }
        return PerformanceMapper.toPagePerformanceResponseDto(performances);
    }

    @Transactional(readOnly = true)
    public Page<PerformanceResponseDto> getPerformancesByReservationPeriod(LocalDateTime start,
        LocalDateTime end, Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpcomingPerformancesByReservationPeriod(
            start, end, pageable);
        if (performances.isEmpty()) {
            throw new ErrorException(NOT_FOUND_PERFORMANCES);
        }
        return PerformanceMapper.toPagePerformanceResponseDto(performances);
    }

    @Transactional(readOnly = true)
    public Page<PerformanceResponseDto> getPerformancesByCategory(Category category,
        Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpcomingPerformancesByCategory(
            LocalDateTime.now(), category, pageable);
        if (performances.isEmpty()) {
            throw new ErrorException(NOT_FOUND_PERFORMANCES);
        }
        return PerformanceMapper.toPagePerformanceResponseDto(performances);
    }
}
