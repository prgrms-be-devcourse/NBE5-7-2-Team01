package com.fifo.ticketing.domain.seat.repository;

import com.fifo.ticketing.domain.seat.entity.Seat;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @EntityGraph(attributePaths = "grade")
    List<Seat> findAllByPerformanceId(Long performanceId);
}
