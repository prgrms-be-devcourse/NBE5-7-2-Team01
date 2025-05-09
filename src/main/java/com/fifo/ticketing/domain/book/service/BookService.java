package com.fifo.ticketing.domain.book.service;

import com.fifo.ticketing.domain.book.dto.BookCreateRequest;
import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.book.entity.BookSeat;
import com.fifo.ticketing.domain.book.repository.BookRepository;
import com.fifo.ticketing.domain.book.repository.BookSeatRepository;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.seat.entity.Seat;
import com.fifo.ticketing.domain.seat.entity.SeatStatus;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.domain.user.repository.UserRepository;
import com.fifo.ticketing.global.exception.AlertDetailException;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.fifo.ticketing.global.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.fifo.ticketing.global.exception.ErrorCode.NOT_FOUND_PERFORMANCE;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;
    private final BookSeatRepository bookSeatRepository;

    @Transactional
    public Long createBook(Long userId, BookCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(NOT_FOUND_MEMBER));

        Performance performance = performanceRepository.findById(request.getPerformanceId())
                .orElseThrow(() -> new ErrorException(NOT_FOUND_PERFORMANCE));

        List<Seat> selectedSeats = seatRepository.findAllById(request.getSeatIds());

        for (Seat seat : selectedSeats) {
            if (!seat.getSeatStatus().equals(SeatStatus.AVAILABLE)) {
                throw new AlertDetailException(ErrorCode.SEAT_ALREADY_BOOKED,
                        String.format("%d번 좌석은 이미 예약되었습니다.", seat.getId()));
            }
        }
        int totalPrice = selectedSeats.stream().mapToInt(Seat::getPrice).sum();
        int quantity = selectedSeats.size();

        Book book = Book.create(user, performance, totalPrice, quantity);
        bookRepository.save(book);

        List<BookSeat> bookSeatList = selectedSeats.stream()
                .map(seat -> BookSeat.of(book, seat))
                .collect(Collectors.toList());

        bookSeatRepository.saveAll(bookSeatList);

        return book.getId();
    }
}
