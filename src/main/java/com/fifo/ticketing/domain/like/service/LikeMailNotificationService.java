package com.fifo.ticketing.domain.like.service;

import static com.fifo.ticketing.global.exception.ErrorCode.NOT_FOUND_PERFORMANCES;

import com.fifo.ticketing.domain.like.entity.Like;
import com.fifo.ticketing.domain.like.repository.LikeRepository;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.global.event.LikeMailEvent;
import com.fifo.ticketing.global.exception.ErrorException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeMailNotificationService {

    private final LikeRepository likeRepository;
    private final LikeMailService likeMailService;
    private final PerformanceRepository performanceRepository;
    private final ApplicationEventPublisher eventPublisher;



    @Transactional
    public boolean sendLikeNotification(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
            .orElseThrow( ()-> new ErrorException(NOT_FOUND_PERFORMANCES));

        List<User> users = likeRepository.findUsersByPerformanceId(performanceId);
        int emailCnt = 0;
        int sendCnt =0;

        for(User user : users) {
            String provider = user.getProvider();
            String email = user.getEmail();

            // 조건: provider가 null이거나 google일 때만 메일 전송
            if ((provider == null || provider.equalsIgnoreCase("google")) &&
                email != null) {

                try {
                    likeMailService.performanceStart(user, performance);
                    sendCnt++;
                } catch (Exception e) {

                    //근데 메일 없는 거는 어떻하지?
                    log.info("메일 전송 실패: {}", email, e);
                }
                emailCnt++;
            }

        }

        return sendCnt == emailCnt;
    }


    @Transactional
    public void sendTimeNotification() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusMinutes(30);

        // 정각으로 하면 안보내는 문제가 있어 +- 1분의 시간을 주었습니다.
        LocalDateTime start = targetTime.minusMinutes(1);
        LocalDateTime end = targetTime.plusMinutes(1);

        List<Like> likes = likeRepository.findLikesByTargetTime(start , end);

        for (Like like : likes) {
            User user = like.getUser();
            Performance performance = like.getPerformance();

            eventPublisher.publishEvent(new LikeMailEvent(user, performance));
            //likeMailService.performanceStart(user, performance);

        }

    }

}
