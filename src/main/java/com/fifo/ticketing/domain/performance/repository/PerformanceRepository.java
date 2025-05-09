package com.fifo.ticketing.domain.performance.repository;

import com.fifo.ticketing.domain.performance.entity.Performance;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    @Query("SELECT p FROM Performance p " +
        "WHERE p.reservationStartTime > :now " +
        "ORDER BY p.reservationStartTime ASC, p.startTime ASC")
    Page<Performance> findUpcomingPerformancesOrderByReservationStartTime(
        @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT p FROM Performance p " +
        "LEFT JOIN LikeCount lc ON lc.performance = p " +
        "WHERE p.reservationStartTime > :now " +
        "ORDER BY COALESCE(lc.likeCount, 0) DESC, p.reservationStartTime ASC")
    Page<Performance> findUpcomingPerformancesOrderByLikes(@Param("now") LocalDateTime now,
        Pageable pageable);

    @Query("SELECT p FROM Performance p " +
        "WHERE p.reservationStartTime BETWEEN :startDate AND :endDate " +
        "ORDER BY p.reservationStartTime ASC, p.startTime ASC")
    Page<Performance> findUpcomingPerformancesByReservationPeriod(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );
}