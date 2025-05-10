package com.fifo.ticketing.domain.like.repository;

import com.fifo.ticketing.domain.like.entity.LikeCount;
import com.fifo.ticketing.domain.performance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeCountRepository extends JpaRepository<LikeCount, Long> {
    Optional<LikeCount> findByPerformance(Performance performance);
}
