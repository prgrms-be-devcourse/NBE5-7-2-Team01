package com.fifo.ticketing.domain.like.dto;


import lombok.AllArgsConstructor;

import lombok.Getter;


public record LikeRequest(Long userId, Long performanceId) {}