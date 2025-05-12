package com.fifo.ticketing.domain.book.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookCompleteDto {
    private String performanceTitle;
    private LocalDateTime performanceStartTime;
    private LocalDateTime performanceEndTime;
    private String placeName;
    private List<BookSeatViewDto> seats;
    private int totalPrice;
    private int quantity;


}
