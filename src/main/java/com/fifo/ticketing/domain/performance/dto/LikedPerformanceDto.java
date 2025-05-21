package com.fifo.ticketing.domain.performance.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class LikedPerformanceDto {

    private Long id;
    private String title;
    private String encodedFileName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String placeName;
    private String urlPrefix;

    public String getUrl() {
        return urlPrefix + encodedFileName;
    }
}
