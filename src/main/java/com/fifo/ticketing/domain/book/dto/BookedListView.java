package com.fifo.ticketing.domain.book.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookedListView {

    private Long bookId;
    private Long performanceId;
    private String performanceTitle;
    private String placeName;
    private List<BookSeatViewDto> seats;
    private int totalPrice;
    private int quantity;
}
