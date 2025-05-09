package com.fifo.ticketing.domain.seat.service;

import com.fifo.ticketing.domain.seat.entity.Seat;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final EntityManager entityManager;

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
}
