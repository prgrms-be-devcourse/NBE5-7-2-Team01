package com.fifo.ticketing.domain.like.service;

import static java.rmi.server.LogStream.log;

import com.fifo.ticketing.domain.like.entity.LikeCount;
import com.fifo.ticketing.domain.like.repository.LikeCountRepository;
import com.fifo.ticketing.domain.like.repository.LikeRepository;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeMailNotificationService {

    private final LikeRepository likeRepository;
    private final LikeMailService likeMailService;

    public boolean sendLikeNotification(Performance performance) {
        List<User> users = likeRepository.findUsersByPerformanceId(performance.getId());
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

}
