package com.fifo.ticketing.domain.performance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceResponseDto {
    Long id;
    String name;
    String address;
    Integer totalSeats;
}
