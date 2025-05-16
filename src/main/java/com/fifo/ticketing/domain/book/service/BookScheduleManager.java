package com.fifo.ticketing.domain.book.service;

import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.book.entity.BookScheduledTask;
import com.fifo.ticketing.domain.book.entity.BookSeat;
import com.fifo.ticketing.domain.book.entity.BookStatus;
import com.fifo.ticketing.domain.book.mapper.BookMapper;
import com.fifo.ticketing.domain.book.repository.BookRepository;
import com.fifo.ticketing.domain.book.repository.BookScheduleRepository;
import com.fifo.ticketing.domain.book.repository.BookSeatRepository;
import com.fifo.ticketing.domain.seat.entity.Seat;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import com.fifo.ticketing.global.util.DateTimeUtil;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookScheduleManager {

    private final BookScheduleRepository bookScheduleRepository;
    private final BookRepository bookRepository;
    private final BookSeatRepository bookSeatRepository;
    @Qualifier("taskScheduler")
    private final TaskScheduler taskScheduler;


    @Transactional
    public void scheduleCancelTask(Long bookId, LocalDateTime runTime) {

        bookScheduleRepository.save(BookMapper.toBookScheduledTaskEntity(bookId, runTime));

        Date triggerTime = DateTimeUtil.toDate(runTime);

        taskScheduler.schedule(() -> cancelIfUnpaid(bookId), triggerTime);

    }

    @Transactional
    public void cancelIfUnpaid(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_BOOK));

        if (book.getBookStatus() == BookStatus.CONFIRMED) {

            book.canceled();

            log.info("{}번 예매 취소됨", bookId);

            List<BookSeat> bookSeats = bookSeatRepository.findAllByBookIdWithSeat(book.getId());
            for (BookSeat bookSeat : bookSeats) {
                Seat seat = bookSeat.getSeat();
                seat.available();
            }
        }

        Optional.ofNullable(book.getScheduledTask())
            .ifPresent(BookScheduledTask::complete);

    }

}
