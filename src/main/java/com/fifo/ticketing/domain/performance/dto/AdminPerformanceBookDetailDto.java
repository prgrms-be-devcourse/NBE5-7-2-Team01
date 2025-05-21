package com.fifo.ticketing.domain.performance.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class AdminPerformanceBookDetailDto {

    private final Long id;
    private final String title;
    private final String encodedFileName;
    private final Long totalPrice;
    private final Long totalQuantity;

    @Setter
    private String urlPrefix;

    public String getUrl() {
        return urlPrefix + encodedFileName;
    }

}
