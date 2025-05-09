package com.fifo.ticketing.domain.book.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BookCreateRequest {
    @NotBlank
    private Long performanceId;
    @NotBlank
    private List<Long> seatIds;

}
