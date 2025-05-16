package com.fifo.ticketing.domain.book.dto;

import com.fifo.ticketing.domain.book.entity.BookStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class BookMailSendDto {

    private String emailAddr;
    private String title;

    private Long performanceId;
    private String performanceTitle;
    private String encodedFileName;
    private LocalDateTime performanceStartTime;
    private LocalDateTime performanceEndTime;
    private String placeName;
    private List<BookSeatViewDto> seats;
    private int totalPrice;
    private int quantity;
    private BookStatus bookStatus;
    private String urlPrefix;

    public String getUrl() {
        return urlPrefix + encodedFileName;
    }

}
