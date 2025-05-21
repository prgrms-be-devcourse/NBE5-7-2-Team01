package com.fifo.ticketing.domain.performance.mapper;

import com.fifo.ticketing.domain.performance.dto.PlaceResponseDto;
import com.fifo.ticketing.domain.performance.entity.Place;

import java.util.List;

public class PlaceMapper {

    public static PlaceResponseDto toDtoForPerformanceCreate(Place place) {
        return PlaceResponseDto.builder()
                .id(place.getId())
                .name(place.getName())
                .build();
    }
}
