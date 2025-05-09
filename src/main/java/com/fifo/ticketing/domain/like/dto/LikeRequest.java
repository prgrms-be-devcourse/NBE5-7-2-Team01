package com.fifo.ticketing.domain.like.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequest {

    private Long userId;
    private Long performanceId;

}
