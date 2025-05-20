package com.fifo.ticketing.domain.performance.repository;

import com.fifo.ticketing.domain.performance.dto.AdminPerformanceBookDetailDto;
import com.fifo.ticketing.domain.performance.dto.AdminPerformanceStaticsDto;
import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.entity.Performance;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    @EntityGraph(attributePaths = {"file"})
    @Query(
        value = "SELECT p FROM Performance p " +
            "WHERE p.startTime > :now " + " AND p.deletedFlag = false "
            + "AND p.title LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY p.reservationStartTime ASC, p.startTime ASC",
        countQuery = "SELECT COUNT(p) FROM Performance p " +
            "WHERE p.startTime > :now AND p.deletedFlag = false " +
            "AND p.title LIKE CONCAT('%', :keyword, '%') "
    )
    Page<Performance> findUpcomingPerformancesByKeywordContaining(@Param("now") LocalDateTime now,
        @Param("keyword") String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"file"})
    @Query(
        value = "SELECT p FROM Performance p " +
            "WHERE p.startTime > :now AND p.deletedFlag = false " +
            "ORDER BY p.reservationStartTime ASC, p.startTime ASC",
        countQuery = "SELECT COUNT(p) FROM Performance p WHERE p.startTime > :now"
    )
    Page<Performance> findUpcomingPerformancesOrderByStartTime(
        @Param("now") LocalDateTime now, Pageable pageable);

    @EntityGraph(attributePaths = {"file"})
    @Query(
        value = "SELECT p FROM Performance p " +
            "LEFT JOIN LikeCount lc ON lc.performance = p " +
            "WHERE p.startTime > :now AND p.deletedFlag = false " +
            "ORDER BY COALESCE(lc.likeCount, 0) DESC, p.reservationStartTime ASC",
        countQuery = "SELECT COUNT(p) FROM Performance p WHERE p.startTime > :now"
    )
    Page<Performance> findUpcomingPerformancesOrderByLikes(
        @Param("now") LocalDateTime now,
        Pageable pageable);

    @EntityGraph(attributePaths = {"file"})
    @Query(
        value = "SELECT p FROM Performance p " +
            "WHERE p.startTime BETWEEN :startDate AND :endDate AND p.deletedFlag = false " +
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
            "WHERE p.startTime > :now AND p.category = :category AND p.deletedFlag = false " +
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
        "WHERE p.deletedFlag != true " +
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
        "AND p.deletedFlag != true " +
        "ORDER BY p.reservationStartTime ASC, p.startTime ASC")
    Page<Performance> findUpcomingPerformancesByCategoryForAdmin(
        @Param("category") Category category, Pageable pageable);

    @Query("SELECT p FROM Performance p " +
        "LEFT JOIN LikeCount lc ON lc.performance = p " +
        "WHERE p.deletedFlag != true " +
        "ORDER BY COALESCE(lc.likeCount, 0) DESC, p.reservationStartTime ASC")
    Page<Performance> findUpcomingPerformancesOrderByLikesForAdmin(Pageable pageable);

    Optional<Performance> findByIdAndDeletedFlagFalse(Long id);

    @Query("SELECT p FROM Performance p " +
        "JOIN FETCH p.file " +
        "WHERE p.deletedFlag = true " +
        "ORDER BY p.reservationStartTime ASC, p.startTime ASC")
    Page<Performance> findUpComingPerformancesByDeletedFlagForAdmin(Pageable pageable);

    @Query("SELECT p.id AS performanceId, p.title AS title, pl.totalSeats AS totalSeats, "
        + "SUM(CASE WHEN b.bookStatus IN ('CONFIRMED', 'PAYED') THEN b.quantity ELSE 0 END) AS reservationCount FROM Performance p "
        + "JOIN Place pl ON p.place.id = pl.id "
        + "LEFT JOIN Book b ON p.id = b.performance.id "
        + "WHERE p.performanceStatus = true "
        + "GROUP BY p.id, p.title, pl.totalSeats")
    Page<AdminPerformanceStaticsDto> findPerformanceStatics(Pageable pageable);

    @Query("SELECT new com.fifo.ticketing.domain.performance.dto.AdminPerformanceBookDetailDto("
        + "p.id, p.title, f.encodedFileName, COALESCE(SUM(b.totalPrice), 0), COALESCE(SUM(b.quantity), 0)) "
        + "FROM Performance p "
        + "LEFT JOIN Book b ON b.performance.id = p.id "
        + "LEFT JOIN File f ON p.file.id = f.id "
        + "WHERE p.deletedFlag = false AND p.id = :performanceId "
        + "GROUP BY p.id, p.title, f.encodedFileName")
    AdminPerformanceBookDetailDto findPerformanceBookDetails(
        @Param("performanceId") Long performanceId);

    @EntityGraph(attributePaths = {"file"})
    @Query(
            value = "SELECT p FROM Performance p " +
                    "WHERE p.startTime > :now "
                    + "AND p.title LIKE CONCAT('%', :keyword, '%') " +
                    "ORDER BY p.reservationStartTime ASC, p.startTime ASC",
            countQuery = "SELECT COUNT(p) FROM Performance p " +
                    "WHERE p.startTime > :now " +
                    "AND p.title LIKE CONCAT('%', :keyword, '%') "
    )
    Page<Performance> findUpcomingPerformancesByKeywordContainingForAdmin(@Param("now") LocalDateTime now,
            @Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE Performance p " +
    "SET p.performanceStatus = true " +
    "WHERE p.reservationStartTime <= :now AND p.performanceStatus = false ")
    void updatePerformanceStatusToReservationStart(@Param("now") LocalDateTime now);
}