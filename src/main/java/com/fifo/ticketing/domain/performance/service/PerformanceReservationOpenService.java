package com.fifo.ticketing.domain.performance.service;

import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerformanceReservationOpenService {

    private final PerformanceRepository performanceRepository;

    @Transactional
    public void updateStatusIfReservationStart() {
        performanceRepository.updatePerformanceStatusToReservationStart(LocalDateTime.now());
    }

}
