package com.fifo.ticketing.domain.performance.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceResponseDto {

    private Long id;
    private String encodedFileName;
    private String title;
    private String category;
    private String place;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime reservationStartTime;
    private boolean performanceStatus;

    @Builder
    public PerformanceResponseDto(Long id, String encodedFileName, String title, String category,
        String place, LocalDateTime startTime, LocalDateTime endTime,
        LocalDateTime reservationStartTime, boolean performanceStatus) {
        this.id = id;
        this.encodedFileName = encodedFileName;
        this.title = title;
        this.category = category;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationStartTime = reservationStartTime;
        this.performanceStatus = performanceStatus;
    }
}
