package com.fifo.ticketing.domain.like.service;

import static com.fifo.ticketing.global.exception.ErrorCode.NOT_FOUND_PERFORMANCES;

import com.fifo.ticketing.domain.book.entity.BookStatus;
import com.fifo.ticketing.domain.book.repository.BookRepository;
import com.fifo.ticketing.domain.like.dto.NoPayedMailDto;
import com.fifo.ticketing.domain.like.dto.ReservationStartMailDto;
import com.fifo.ticketing.domain.like.entity.Like;
import com.fifo.ticketing.domain.like.mapper.LikeMailMapper;
import com.fifo.ticketing.domain.like.repository.LikeRepository;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.global.event.NoPayMailEvent;
import com.fifo.ticketing.global.event.ReservationEvent;
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
    private final BookRepository bookRepository;
    private final SeatRepository seatRepository;



    @Transactional
    public void sendTimeNotification() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusMinutes(30);

        LocalDateTime start = targetTime.minusMinutes(30);
        LocalDateTime end = targetTime.plusMinutes(30);

        likeRepository.findLikesByTargetTime(start, end).stream()
            .filter(like -> isEmailSendTarget(like.getUser()))
            .map(like -> {
                User user = like.getUser();
                Performance performance = like.getPerformance();
                return LikeMailMapper.toReservationStartMailDto(user, performance);
            })
            .forEach(dto -> eventPublisher.publishEvent(new ReservationEvent(dto)));

    }


    @Transactional
    public void sendNoPayedNotification() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationTime = now.minusMinutes(60);

        LocalDateTime start = reservationTime.minusMinutes(30);
        LocalDateTime end = reservationTime.plusMinutes(30);

        likeRepository.findLikesByTargetTime(start, end)
            .forEach(this::notifyNoPay);
    }

    private void notifyNoPay(Like like) {
        User user = like.getUser();
        Performance performance = like.getPerformance();
        boolean payed = bookRepository.existsByUserAndPerformanceAndBookStatus(user, performance, BookStatus.PAYED);

        if (!isEmailSendTarget(user)) {
            return; //  메일 전송 조건 안되면 skip
        }

        if(payed){return;}
        int availableSeats = seatRepository.countAvailableSeatsByPerformanceId(performance.getId());

        log.info("예약 여부: {}, 잔여좌석: {}", payed, availableSeats);

        NoPayedMailDto dto = LikeMailMapper.toNoPayedMailDto(user, performance, availableSeats);
        eventPublisher.publishEvent(new NoPayMailEvent(dto));
    }


    private boolean isEmailSendTarget(User user) {
        String provider = user.getProvider();
        String email = user.getEmail();
        return (provider == null || provider.equalsIgnoreCase("google")) && email != null;
    }

}
