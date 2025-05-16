package com.fifo.ticketing.domain.like.repository;

import com.fifo.ticketing.domain.like.entity.Like;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByUserAndPerformance(User user, Performance performance);

    @Query("SELECT l.user FROM Like l WHERE l.performance.id = :performanceId AND l.isLiked = true")
    List<User> findUsersByPerformanceId(@Param("performanceId") Long performanceId);

    @Query("SELECT l FROM Like l WHERE l.isLiked =true AND l.performance.reservationStartTime BETWEEN :start AND :end")
    List<Like> findLikesByTargetTime(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 해당 Method가 사용되지 않는 것 같은데, 제거 해도 괜찮을까요?
    @Query("SELECT l.performance.id FROM Like l WHERE l.user.id = :userId AND l.isLiked = true")
    List<Long> findLikedPerformanceIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT l.performance FROM Like l WHERE l.performance.deletedFlag != true AND l.user.id = :userId AND l.isLiked = true ORDER BY l.performance.startTime ASC")
    Page<Performance> findLikedPerformancesByUserId(@Param("userId") Long userId, Pageable pageable);

}
