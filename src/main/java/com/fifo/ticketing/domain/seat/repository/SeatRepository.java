package com.fifo.ticketing.domain.seat.repository;

import com.fifo.ticketing.domain.seat.entity.Seat;

import com.fifo.ticketing.domain.seat.entity.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT s FROM Seat s JOIN FETCH s.grade WHERE s.performance.id = :performanceId AND s.seatStatus <> 'DELETED'")
    List<Seat> findValidSeatsByPerformanceId(@Param("performanceId") Long performanceId);


    @Modifying(clearAutomatically = true)
    @Query("UPDATE Seat s SET s.seatStatus = :status WHERE s.performance.id = :performanceId")
    void updateSeatStatusByPerformanceId(@Param("performanceId") Long performanceId, @Param("status") SeatStatus status);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM Seat s WHERE s.id IN :seatIds")
    List<Seat> findAllByIdInWithOptimisticLock(@Param("seatIds") List<Long> seatIds);

    //공연의 남은 좌석 수 계산
    @Query("select count(*) From Seat s where s.performance.id = :performanceId AND s.seatStatus = 'AVAILABLE'")
    int countAvailableSeatsByPerformanceId(@Param("performanceId") Long performanceId);

}
