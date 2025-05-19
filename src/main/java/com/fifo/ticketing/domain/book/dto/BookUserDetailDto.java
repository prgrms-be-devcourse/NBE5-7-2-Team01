package com.fifo.ticketing.domain.book.dto;

import com.fifo.ticketing.domain.book.entity.BookStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class BookUserDetailDto {

    private final Long bookId; //bookId
    private final Long performanceId;
    private final String title;
    private final Integer totalPrice;
    private final Integer quantity;
    private final String username;
    private final String encodedFileName;
    private final BookStatus bookStatus;

    @Setter
    private String urlPrefix;

    public String getUrl() {
        return urlPrefix + encodedFileName;
    }

}
