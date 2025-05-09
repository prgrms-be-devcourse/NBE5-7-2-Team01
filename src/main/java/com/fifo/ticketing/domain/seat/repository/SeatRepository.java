package com.fifo.ticketing.domain.seat.repository;

import com.fifo.ticketing.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByPerformanceId(Long performanceId);
}
