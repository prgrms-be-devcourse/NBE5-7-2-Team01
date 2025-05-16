package com.fifo.ticketing.domain.book.dto;

import com.fifo.ticketing.domain.seat.entity.SeatStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class BookCompleteDto {

    private Long performanceId;
    private String performanceTitle;
    private String encodedFileName;
    private LocalDateTime performanceStartTime;
    private LocalDateTime performanceEndTime;
    private String placeName;
    private List<BookSeatViewDto> seats;
    private int totalPrice;
    private int quantity;
    @Setter
    private boolean paymentCompleted;
    private String urlPrefix;

    public String getUrl() {
        return urlPrefix + encodedFileName;
    }

}
