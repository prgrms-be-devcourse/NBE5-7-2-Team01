package com.fifo.ticketing.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookResponse {
    private Long bookId;

    private int totalPrice;

    private int quantity;

}
