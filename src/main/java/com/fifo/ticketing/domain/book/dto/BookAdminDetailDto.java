package com.fifo.ticketing.domain.book.dto;

public record BookAdminDetailDto(Long id,
                                 String username,
                                 Integer totalPrice,
                                 Integer quantity) {

}
