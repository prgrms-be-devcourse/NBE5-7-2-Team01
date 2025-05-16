package com.fifo.ticketing.domain.user.service;

import com.fifo.ticketing.domain.like.repository.LikeRepository;
import com.fifo.ticketing.domain.performance.dto.LikedPerformanceDto;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.mapper.PerformanceMapper;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    @Value("${file.url-prefix}")
    private String urlPrefix;

    private final LikeRepository likeRepository;

    @Transactional(readOnly = true)
    public Page<LikedPerformanceDto> getUserLikedPerformance(Long userId,
        Pageable pageable) {
        if (userId != null) {
            Page<Performance> likedPerformancesByUserId = likeRepository.findLikedPerformancesByUserId(
                userId, pageable);
            if (likedPerformancesByUserId.isEmpty()) {
                return Page.empty();
            }
            return PerformanceMapper.toPageLikedPerformanceDto(likedPerformancesByUserId,
                urlPrefix);
        } else {
            throw new ErrorException(ErrorCode.NOT_FOUND_MEMBER);
        }
    }

}
