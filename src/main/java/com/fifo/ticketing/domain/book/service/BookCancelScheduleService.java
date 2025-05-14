package com.fifo.ticketing.domain.book.service;

import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.book.entity.BookScheduledTask;
import com.fifo.ticketing.domain.book.entity.BookSeat;
import com.fifo.ticketing.domain.book.entity.BookStatus;
import com.fifo.ticketing.domain.book.repository.BookRepository;
import com.fifo.ticketing.domain.book.repository.BookScheduleRepository;
import com.fifo.ticketing.domain.book.repository.BookSeatRepository;
import com.fifo.ticketing.domain.seat.entity.Seat;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookCancelScheduleService {

    private final BookRepository bookRepository;
    private final BookSeatRepository bookSeatRepository;
    private final BookScheduleRepository bookScheduleRepository;

    @Transactional
    public void cancelIfUnpaid(Long bookId, Long taskId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_BOOK));

        if (book.getBookStatus() == BookStatus.CONFIRMED) {

            List<BookSeat> bookSeats = bookSeatRepository.findAllByBookId(book.getId());
            book.canceled();

            log.info("{}번 예매 취소됨", bookId);

            for (BookSeat bookSeat : bookSeats) {
                Seat seat = bookSeat.getSeat();
                seat.available();
            }
        }

        bookScheduleRepository.findById(taskId)
            .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_SCHEDULE))
            .complete();
    }

}
