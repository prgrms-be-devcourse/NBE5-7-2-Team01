package com.fifo.ticketing.global.event;

import com.fifo.ticketing.domain.book.entity.Book;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PerformanceCanceledEvent {
    private final List<Book> canceledBooks;
}
