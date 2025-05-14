package com.fifo.ticketing.domain.book.service;

import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.book.entity.BookSeat;
import com.fifo.ticketing.domain.book.entity.BookStatus;
import com.fifo.ticketing.domain.book.repository.BookRepository;
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
public class BookCancelService {

    private final BookRepository bookRepository;
    private final BookSeatRepository bookSeatRepository;

    @Transactional
    public void cancelIfUnpaid(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_BOOK));

        if (book.getBookStatus() == BookStatus.CONFIRMED) {

            List<BookSeat> bookSeats = bookSeatRepository.findAllByBookId(book.getId());
            book.canceled();

            for (BookSeat bookSeat : bookSeats) {
                Seat seat = bookSeat.getSeat();
                seat.available();
            }

            log.info("===================스케줄러=====================");
            log.info("{}번 예매 생성 시간", book.getCreatedAt());
            log.info("{}번 예매 자동 취소", book.getId());
            log.info("{}번 예매 취소 시간", book.getUpdatedAt());
            log.info("==============================================");

        }

    }



}
