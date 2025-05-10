package com.fifo.ticketing.domain.like.dto;


import lombok.AllArgsConstructor;

import lombok.Getter;


@Getter
@AllArgsConstructor
public class LikeRequest {

    private final Long userId;
    private final Long performanceId;

}
