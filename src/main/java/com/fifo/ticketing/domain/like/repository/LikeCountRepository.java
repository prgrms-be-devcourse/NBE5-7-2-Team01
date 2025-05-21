package com.fifo.ticketing.domain.like.repository;

import com.fifo.ticketing.domain.like.entity.LikeCount;
import com.fifo.ticketing.domain.performance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeCountRepository extends JpaRepository<LikeCount, Long> {
    Optional<LikeCount> findByPerformance(Performance performance);
}
