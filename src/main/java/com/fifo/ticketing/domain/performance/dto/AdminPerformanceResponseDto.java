package com.fifo.ticketing.domain.performance.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminPerformanceResponseDto {

    private Long id;
    private String description;
    private String encodedFileName;
    private String title;
    private String category;
    private String place;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime reservationStartTime;
    private boolean performanceStatus;
    private String urlPrefix;

    @Builder
    public AdminPerformanceResponseDto(Long id, String encodedFileName, String title, String description,
                                       String category, String place, LocalDateTime startTime, LocalDateTime endTime,
                                       LocalDateTime reservationStartTime, boolean performanceStatus, String urlPrefix) {
        this.id = id;
        this.encodedFileName = encodedFileName;
        this.title = title;
        this.description = description;
        this.category = category;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationStartTime = reservationStartTime;
        this.performanceStatus = performanceStatus;
        this.urlPrefix = urlPrefix;
    }

    public String getUrl() {
        return urlPrefix + encodedFileName;
    }
}
