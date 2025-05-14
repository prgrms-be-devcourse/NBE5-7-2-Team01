package com.fifo.ticketing.domain.performance.repository;

import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.entity.Performance;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    @EntityGraph(attributePaths = {"file"})
    @Query(
        value = "SELECT p FROM Performance p " +
            "WHERE p.startTime > :now " +
            "ORDER BY p.reservationStartTime ASC, p.startTime ASC",
        countQuery = "SELECT COUNT(p) FROM Performance p WHERE p.startTime > :now"
    )
    Page<Performance> findUpcomingPerformancesOrderByStartTime(
        @Param("now") LocalDateTime now, Pageable pageable);

    @EntityGraph(attributePaths = {"file"})
    @Query(
        value = "SELECT p FROM Performance p " +
            "LEFT JOIN LikeCount lc ON lc.performance = p " +
            "WHERE p.startTime > :now " +
            "ORDER BY COALESCE(lc.likeCount, 0) DESC, p.reservationStartTime ASC",
        countQuery = "SELECT COUNT(p) FROM Performance p WHERE p.startTime > :now"
    )
    Page<Performance> findUpcomingPerformancesOrderByLikes(
        @Param("now") LocalDateTime now,
        Pageable pageable);

    @EntityGraph(attributePaths = {"file"})
    @Query(
        value = "SELECT p FROM Performance p " +
            "WHERE p.startTime BETWEEN :startDate AND :endDate " +
            "ORDER BY p.reservationStartTime ASC, p.startTime ASC",
        countQuery = "SELECT COUNT(p) FROM Performance p " +
            "WHERE p.startTime BETWEEN :startDate AND :endDate"
    )
    Page<Performance> findUpcomingPerformancesByReservationPeriod(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable);

    @EntityGraph(attributePaths = {"file"})
    @Query(
        value = "SELECT p FROM Performance p " +
            "WHERE p.startTime > :now AND p.category = :category " +
            "ORDER BY p.reservationStartTime ASC, p.startTime ASC",
        countQuery = "SELECT COUNT(p) FROM Performance p " +
            "WHERE p.startTime > :now AND p.category = :category"
    )
    Page<Performance> findUpcomingPerformancesByCategory(
        @Param("now") LocalDateTime now,
        @Param("category") Category category,
        Pageable pageable);

    @Query("SELECT p FROM Performance p " +
        "JOIN FETCH p.file " +
        "ORDER BY p.reservationStartTime ASC, p.startTime ASC")
    Page<Performance> findUpcomingPerformancesOrderByReservationStartTimeForAdmin(
        Pageable pageable);

    @Query("SELECT p FROM Performance p " +
        "WHERE p.reservationStartTime BETWEEN :startDate AND :endDate " +
        "ORDER BY p.reservationStartTime ASC, p.startTime ASC")
    Page<Performance> findUpcomingPerformancesByReservationPeriodForAdmin(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );

    @Query("SELECT p FROM Performance p " +
        "WHERE p.category = :category " +
        "ORDER BY p.reservationStartTime ASC, p.startTime ASC")
    Page<Performance> findUpcomingPerformancesByCategoryForAdmin(
        @Param("category") Category category, Pageable pageable);

    @Query("SELECT p FROM Performance p" +
        " LEFT JOIN LikeCount lc ON lc.performance = p " +
        " ORDER BY COALESCE(lc.likeCount, 0) DESC, p.reservationStartTime ASC")
    Page<Performance> findUpcomingPerformancesOrderByLikesForAdmin(Pageable pageable);

}