package com.fifo.ticketing.domain.book.service;

import static com.fifo.ticketing.global.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.fifo.ticketing.global.exception.ErrorCode.NOT_FOUND_PERFORMANCE;

import com.fifo.ticketing.domain.book.dto.BookCompleteDto;
import com.fifo.ticketing.domain.book.dto.BookCreateRequest;
import com.fifo.ticketing.domain.book.dto.BookedView;
import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.book.entity.BookSeat;
import com.fifo.ticketing.domain.book.entity.BookStatus;
import com.fifo.ticketing.domain.book.mapper.BookMapper;
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
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    @Value("${file.url-prefix}")
    private String urlPrefix;

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;
    private final BookSeatRepository bookSeatRepository;
    private final BookScheduleManager bookScheduleManager;

    @Transactional
    public Long createBook(Long performanceId, Long userId, BookCreateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ErrorException(NOT_FOUND_MEMBER));

        Performance performance = performanceRepository.findById(performanceId)
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

        Book book = BookMapper.toBookEntity(user, performance, totalPrice, quantity);
        bookRepository.save(book);
        bookRepository.flush();

        List<BookSeat> bookSeatList = BookMapper.toBookSeatEntities(book, selectedSeats);

        bookSeatRepository.saveAll(bookSeatList);

        for (Seat seat : selectedSeats) {
            seat.book();
        }

        Long bookId = book.getId();

        LocalDateTime runTime = LocalDateTime.now().plusMinutes(5);

        bookScheduleManager.scheduleCancelTask(bookId, runTime);

        return bookId;
    }

    @Transactional
    public BookCompleteDto getBookCompleteInfo(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_BOOK));

        return BookMapper.toBookCompleteDto(book, urlPrefix);
    }

    @Transactional
    public void completePayment(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_BOOK));

        List<BookSeat> bookSeats = bookSeatRepository.findAllByBookId(book.getId());

        book.payed();

        for (BookSeat bookSeat : bookSeats) {
            Seat seat = bookSeat.getSeat();
            seat.occupy();
        }

    }

    @Transactional
    public Long cancelBook(Long bookId, Long userId) {
        Book book = bookRepository.findByUserIdAndId(userId, bookId)
            .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_BOOK));

        List<BookSeat> bookSeats = bookSeatRepository.findAllByBookId(book.getId());

        book.canceled();

        for (BookSeat bookSeat : bookSeats) {
            Seat seat = bookSeat.getSeat();
            seat.available();
        }

        return bookId;
    }


    @Transactional
    public List<Book> cancelAllBook(Performance performance) {
        bookRepository.cancelAllByPerformance(performance, BookStatus.ADMIN_REFUNDED,
            BookStatus.PAYED);
        return bookRepository.findAllWithUserAndPerformanceByPerformanceAndBookStatus(performance,
            BookStatus.ADMIN_REFUNDED);
    }

    public Page<BookedView> getBookedList(Long userId, String title, BookStatus status,
        Pageable pageable) {
        Page<Book> bookPage;
        if (title != null && status != null) {
            bookPage = bookRepository.findAllByUserIdAndTitleAndBookStatus(
                userId, title, status, pageable);
        } else if (title != null) {
            bookPage = bookRepository.findAllByUserIdAndTitle(userId, title,
                pageable);
        } else if (status != null) {
            bookPage = bookRepository.findAllByUserIdAndBookStatus(userId, status,
                pageable);
        } else {
            bookPage = bookRepository.findAllByUserId(userId, pageable);
        }

        return BookMapper.toBookedViewDtoList(bookPage, urlPrefix);
    }

    @Transactional
    public BookedView getBookDetail(Long userId, Long bookId) {
        Book book = bookRepository.findByUserIdAndId(userId, bookId)
            .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_BOOK));

        return BookMapper.toBookedViewDto(book, urlPrefix);
    }
}
