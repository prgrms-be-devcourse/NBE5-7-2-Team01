package com.fifo.ticketing.domain.like.controller;


import com.fifo.ticketing.domain.like.service.LikeMailNotificationService;
import com.fifo.ticketing.domain.like.service.LikeMailService;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/notifications")
@RequiredArgsConstructor
public class LikeNotificationController {

    private final LikeMailNotificationService likeMailNotificationService;

    @PostMapping("/performance/{performanceId}/likes")
    public ResponseEntity<String> performanceLike(@PathVariable("performanceId") Long performanceId) {

        boolean result = likeMailNotificationService.sendLikeNotification(performanceId);

        String message = result ? "모든 이메일 전송 완료" : "일부 이메일 전송 실패";
        return ResponseEntity.ok(message);
    }


}
