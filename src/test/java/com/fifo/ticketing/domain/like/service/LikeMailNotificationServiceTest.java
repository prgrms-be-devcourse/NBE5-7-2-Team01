package com.fifo.ticketing.domain.like.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fifo.ticketing.domain.like.repository.LikeRepository;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.global.Event.LikeMailEvent;
import com.fifo.ticketing.global.Event.LikeMailEventListener;
import com.fifo.ticketing.global.Event.MailType;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.context.ApplicationEventPublisher;



class LikeMailNotificationServiceTest {

    @InjectMocks
    private LikeMailNotificationService likeMailNotificationService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private LikeMailService likeMailService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;


    private Performance performance;
    private final Long performanceId = 100L;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        performance = Performance.builder().id(performanceId).title("테스트 공연").build();
    }

    @Test
    void 좋아요한_유저에게_메일_정상발송() {
        User user = User.builder()
            .id(1L)
            .email("hseo6480@gmail.com")
            .username("홍길동")
            .provider("google")
            .build();

        when(performanceRepository.findById(performanceId)).thenReturn(Optional.of(performance));
        when(likeRepository.findUsersByPerformanceId(performanceId)).thenReturn(List.of(user));

        boolean result = likeMailNotificationService.sendLikeNotification(performanceId);

        assertThat(result).isTrue();
        verify(likeMailService).performanceStart(user, performance);
    }

    @Test
    void 이벤트_리스너테스트() {
        // given
        User user = User.builder()
            .email("test@email.com")
            .username("테스트")
            .build();

        Performance performance = Performance.builder()
            .title("테스트 공연")
            .build();

        LikeMailEvent event = new LikeMailEvent(user, performance, MailType.RESERVATION_NOTICE);

        // when: 이벤트 리스너 직접 호출
        new LikeMailEventListener(likeMailService).handleLikeMailEvent(event);

        // then
        verify(likeMailService).performanceStart(user, performance);
    }
}