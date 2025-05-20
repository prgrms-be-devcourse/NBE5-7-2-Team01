package com.fifo.ticketing.domain.like.controller;


import com.fifo.ticketing.domain.like.service.LikeMailNotificationService;
import com.fifo.ticketing.domain.like.service.LikeMailService;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/notifications")
@RequiredArgsConstructor
@Tag(name = "Admin_Like_Notification", description = "관리자에 의한 메일 전송 API")
// 해당 부분 호출 위치가 파악되지 않습니다.
// 스케쥴러에서는 따로 likeMailNotificationService.sendTimeNotification();를 통해서 전송하는 것 같은데
// 아마 제가 못 찾는 것 같은데 어느 화면에서 이용하는 것일까요?
public class LikeNotificationController {

    private final LikeMailNotificationService likeMailNotificationService;

    @PostMapping("/performance/{performanceId}/likes")
    public ResponseEntity<String> performanceLike(@ PathVariable("performanceId") Long performanceId) {

        boolean result = likeMailNotificationService.sendLikeNotification(performanceId);

        return result
            ? ResponseEntity.ok("모든 이메일 전송 완료 ")
            : ResponseEntity.status(207).body("일부 이메일 전송 실패 ");

    }


}
