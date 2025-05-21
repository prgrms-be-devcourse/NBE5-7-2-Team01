package com.fifo.ticketing.domain.performance.dto;

public interface AdminPerformanceStaticsDto {

    Long getPerformanceId();

    String getTitle();

    Integer getTotalSeats();

    Long getReservationCount();
}
