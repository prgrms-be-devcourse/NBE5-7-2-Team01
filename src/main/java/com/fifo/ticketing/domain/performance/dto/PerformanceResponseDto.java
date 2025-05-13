package com.fifo.ticketing.domain.performance.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceResponseDto {

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
    public PerformanceResponseDto(Long id, String encodedFileName, String title, String description, 
        String category, String place, LocalDateTime startTime, LocalDateTime endTime,
        LocalDateTime reservationStartTime, boolean performanceStatus, String urlPrefix) {
        this.id = id;
        this.encodedFileName = encodedFileName;
        this.id = performanceId;
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
