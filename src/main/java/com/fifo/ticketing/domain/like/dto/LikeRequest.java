package com.fifo.ticketing.domain.like.dto;


import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeRequest {

    private Long userId;
    private Long performanceId;

}
