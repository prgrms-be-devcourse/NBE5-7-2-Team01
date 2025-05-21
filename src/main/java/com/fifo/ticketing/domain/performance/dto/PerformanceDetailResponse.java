package com.fifo.ticketing.domain.performance.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PerformanceDetailResponse {

    private Long performanceId;
    private String title;
    private String description;
    private String placeName;
    private String encodedFileName;
    private String address;
    private String category;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime reservationStartTime;
    private boolean performanceStatus;
    private int totalSeats;
    private List<PerformanceSeatGradeDto> seatGrades;
    private String urlPrefix;

    public String getUrl() {
        return urlPrefix + encodedFileName;
    }
}
