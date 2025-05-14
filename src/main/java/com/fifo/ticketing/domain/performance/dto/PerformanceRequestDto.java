package com.fifo.ticketing.domain.performance.dto;

import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceRequestDto {
    private String title;
    private String description;
    private Category category;
    // true : 예매 가능 / false : 예매 불가능
    private boolean performanceStatus;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime reservationStartTime;
    private Long placeId;
}
