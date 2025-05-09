package com.fifo.ticketing.domain.like.service;

import com.fifo.ticketing.domain.like.dto.LikeRequest;
import com.fifo.ticketing.domain.like.entity.Like;
import com.fifo.ticketing.domain.like.entity.LikeCount;
import com.fifo.ticketing.domain.like.repository.LikeCountRepository;
import com.fifo.ticketing.domain.like.repository.LikeRepository;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeCountRepository likeCountRepository;
    private final UserRepository userRepository;
    private final PerformanceRepository performanceRepository;

    public boolean toggleLike(LikeRequest likeRequest) {

        User user = userRepository.findById(likeRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("not found"));

        Performance performance = performanceRepository.findById(likeRequest.getPerformanceId())
                .orElseThrow( ()-> new EntityNotFoundException("Performance not found"))    ;

        Like existingLike = likeRepository.findByUserAndPerformance(user, performance);

        //like가 없다면
        if(existingLike == null) {
            Like like = Like.builder()
                    .user(user)
                    .performance(performance)
                    .isLiked(true)
                    .build();
            likeRepository.save(like);
            updateLike(performance,1);
            // likecount 제거
            return true;
        }//없다ㅕㄴ

        if(existingLike.isLiked() ){
            existingLike.setLiked(false);
            likeRepository.save(existingLike);
            updateLike(performance, -1);
            // likeCOunt 추가
            return false;
        }else{
            existingLike.setLiked(true);
            likeRepository.save(existingLike);
            updateLike(performance,1);
            return true;

        }

    }

    private void updateLike(Performance performance, int cnt) {
        LikeCount likeCount = likeCountRepository.findByPerformance(performance)
                .orElseThrow(() -> new EntityNotFoundException("not found"));

        long updatedCnt = likeCount.getLikeCount() + cnt;
        updatedCnt = Math.max(0, updatedCnt); //  음수 방지
        likeCount.setLikeCount(updatedCnt);
        likeCountRepository.save(likeCount);

    }
}
