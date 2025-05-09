package com.fifo.ticketing.domain.like.repository;

import com.fifo.ticketing.domain.like.entity.Like;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserAndPerformance(User user, Performance performance);
}
