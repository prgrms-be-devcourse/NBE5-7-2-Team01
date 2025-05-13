package com.fifo.ticketing.domain.performance.dto;

import com.fifo.ticketing.domain.performance.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
    private boolean performanceStatus;
    private int totalSeats;
    private List<PerformanceSeatGradeDto> seatGrades;


}
