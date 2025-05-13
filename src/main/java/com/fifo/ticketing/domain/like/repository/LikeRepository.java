package com.fifo.ticketing.domain.like.repository;

import com.fifo.ticketing.domain.like.entity.Like;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByUserAndPerformance(User user, Performance performance);

    @Query("SELECT l.user FROM Like l WHERE l.performance.id = :performanceId AND l.isLiked = true")
    List<User> findUsersByPerformanceId(@Param("performanceId") Long performanceId);

    @Query("SELECT l.performance.id FROM Like l WHERE l.user.id = :userId AND l.isLiked = true")
    List<Long> findLikedPerformanceIdsByUserId(@Param("userId") Long userId);
}
