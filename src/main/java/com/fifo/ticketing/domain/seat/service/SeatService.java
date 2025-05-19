package com.fifo.ticketing.domain.seat.service;

import static com.fifo.ticketing.global.exception.ErrorCode.SEAT_ALREADY_BOOKED;

import com.fifo.ticketing.domain.book.dto.BookSeatViewDto;
import com.fifo.ticketing.domain.book.entity.BookSeat;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.seat.entity.SeatStatus;
import com.fifo.ticketing.domain.seat.mapper.SeatMapper;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import com.fifo.ticketing.domain.seat.entity.Seat;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final EntityManager entityManager;

    public static void changeSeatStatus(List<BookSeat> bookSeats, SeatStatus newStatus) {
        for (BookSeat bookSeat : bookSeats) {
            Seat seat = bookSeat.getSeat();
            switch (newStatus) {
                case OCCUPIED -> seat.occupy();
                case AVAILABLE -> seat.available();
                default -> throw new ErrorException(ErrorCode.NOT_FOUND_SEAT_STATUS);
            }
        }
    }

    public List<Seat> validateBookSeats(List<Long> seatIds) {
        List<Seat> selectedSeats = seatRepository.findAllByIdInWithOptimisticLock(seatIds);

        for (Seat seat : selectedSeats) {
            seat.validateAvailable();
            seat.book();
        }

        try {
            seatRepository.flush();
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ErrorException(SEAT_ALREADY_BOOKED);
        }
        return selectedSeats;
    }

    public List<BookSeatViewDto> getSeatsForPerformance(Long performanceId) {
        return seatRepository.findAllByPerformanceId(performanceId)
            .stream()
            .map(SeatMapper::toBookSeatViewDto)
            .collect(Collectors.toList());
    }
    

    @Transactional
    public void createSeats(List<Seat> seatList) {
        int batchSize = 100;
        for (int i = 0; i < seatList.size(); i++) {
            entityManager.persist(seatList.get(i));
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void deleteSeatsByPerformanceId(Long performanceId) {
        seatRepository.updateSeatStatusByPerformanceId(performanceId, SeatStatus.DELETED);
    }
}
