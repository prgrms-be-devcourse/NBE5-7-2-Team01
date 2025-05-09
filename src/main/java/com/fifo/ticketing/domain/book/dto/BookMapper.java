package com.fifo.ticketing.domain.book.dto;

import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.book.entity.BookSeat;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.seat.entity.Seat;
import com.fifo.ticketing.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {

    public static Book toBookEntity(User user, Performance performance, int totalPrice, int quantity) {
        return Book.create(user, performance, totalPrice, quantity);
    }

    public static List<BookSeat> toBookSeatEntities(Book book, List<Seat> seats) {
        return seats.stream()
                .map(seat -> BookSeat.of(book, seat))
                .collect(Collectors.toList());
    }
}
