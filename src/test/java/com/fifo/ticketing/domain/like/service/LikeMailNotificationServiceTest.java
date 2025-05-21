package com.fifo.ticketing.domain.like.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.fifo.ticketing.domain.book.entity.BookStatus;
import com.fifo.ticketing.domain.book.repository.BookRepository;
import com.fifo.ticketing.domain.like.entity.Like;
import com.fifo.ticketing.domain.like.mapper.LikeMailMapper;
import com.fifo.ticketing.domain.like.repository.LikeRepository;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.global.event.NoPayMailEvent;
import com.fifo.ticketing.global.event.ReservationEvent;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class LikeMailNotificationServiceTest {

    @Mock private LikeRepository likeRepository;
    @Mock private LikeMailService likeMailService;
    @Mock private PerformanceRepository performanceRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private BookRepository bookRepository;
    @Mock private SeatRepository seatRepository;

    @InjectMocks
    private LikeMailNotificationService likeMailNotificationService;

    private User user;
    private Performance performance;
    private Like like;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .id(1L)
            .email("test@gmail.com")
            .username("홍길동")
            .provider("google")
            .build();

        performance = Performance.builder()
            .id(1L)
            .title("테스트 공연")
            .startTime(LocalDateTime.now().plusHours(1))
            .build();

        like = Like.builder()
            .id(1L)
            .user(user)
            .performance(performance)
            .build();
    }

    @Test
    void 공연30분전_알림이벤트_정상발송() {
        // given
        when(likeRepository.findLikesByTargetTime(any(), any()))
            .thenReturn(List.of(like));

        // when
        likeMailNotificationService.sendTimeNotification();

        // then
        verify(eventPublisher).publishEvent(any(ReservationEvent.class));
    }

    @Test
    void 결제안한유저_알림이벤트_정상발송() {
        // given
        when(likeRepository.findLikesByTargetTime(any(), any()))
            .thenReturn(List.of(like));
        when(bookRepository.existsByUserAndPerformanceAndBookStatus(user, performance, BookStatus.PAYED))
            .thenReturn(false);
        when(seatRepository.countAvailableSeatsByPerformanceId(performance.getId()))
            .thenReturn(10);

        // when
        likeMailNotificationService.sendNoPayedNotification();

        // then
        verify(eventPublisher).publishEvent(any(NoPayMailEvent.class));
    }

    @Test
    void 결제완료한유저는_알림이벤트_발송되지_않음() {
        // given
        when(likeRepository.findLikesByTargetTime(any(), any()))
            .thenReturn(List.of(like));
        when(bookRepository.existsByUserAndPerformanceAndBookStatus(user, performance, BookStatus.PAYED))
            .thenReturn(true); // 결제 완료

        // when
        likeMailNotificationService.sendNoPayedNotification();

        // then
        verify(eventPublisher, never()).publishEvent(any(NoPayMailEvent.class));
    }
}
