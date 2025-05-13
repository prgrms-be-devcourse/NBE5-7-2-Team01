package com.fifo.ticketing.domain.seat.service;

import com.fifo.ticketing.domain.book.dto.BookSeatViewDto;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.seat.entity.SeatStatus;
import com.fifo.ticketing.domain.seat.mapper.SeatMapper;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import com.fifo.ticketing.domain.seat.entity.Seat;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final EntityManager entityManager;

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
