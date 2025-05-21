package com.fifo.ticketing.domain.performance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "공연 응답 DTO")
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
    private String urlPrefix;

    @Builder
    public PerformanceResponseDto(Long id, String encodedFileName, String title,
        String category, String place, LocalDateTime startTime, LocalDateTime endTime,
        LocalDateTime reservationStartTime, boolean performanceStatus, String urlPrefix) {
        this.id = id;
        this.encodedFileName = encodedFileName;
        this.title = title;
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
