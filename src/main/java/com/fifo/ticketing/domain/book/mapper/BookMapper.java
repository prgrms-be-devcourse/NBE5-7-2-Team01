package com.fifo.ticketing.domain.book.mapper;

import com.fifo.ticketing.domain.book.dto.BookCompleteDto;
import com.fifo.ticketing.domain.book.dto.BookedView;
import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.book.entity.BookScheduledTask;
import com.fifo.ticketing.domain.book.entity.BookSeat;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.seat.entity.Seat;
import com.fifo.ticketing.domain.seat.mapper.SeatMapper;
import com.fifo.ticketing.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public static Book toBookEntity(User user, Performance performance, int totalPrice, int quantity) {
        return Book.create(user, performance, totalPrice, quantity);
    }

    public static List<BookSeat> toBookSeatEntities(Book book, List<Seat> seats) {
        return seats.stream()
                .map(seat -> BookSeat.of(book, seat))
                .collect(Collectors.toList());
    }

    public static BookCompleteDto toBookCompleteDto(Book book) {
        return BookCompleteDto.builder()
            .performanceId(book.getPerformance().getId())
            .performanceTitle(book.getPerformance().getTitle())
            .encodedFileName(book.getPerformance().getFile().getEncodedFileName())
            .performanceStartTime(book.getPerformance().getStartTime())
            .performanceEndTime(book.getPerformance().getEndTime())
            .placeName(book.getPerformance().getPlace().getName())
            .seats(book.getBookSeats().stream()
                .map(bs -> SeatMapper.toBookSeatViewDto(bs.getSeat()))
                .collect(Collectors.toList()))
            .totalPrice(book.getTotalPrice())
            .quantity(book.getQuantity())
            .paymentCompleted(false)
            .build();
    }

    public static BookedView toBookedViewDto(Book book) {
        Performance performance = book.getPerformance();

        return BookedView.builder()
            .bookId(book.getId())
            .performanceId(performance.getId())
            .performanceTitle(performance.getTitle())
            .encodedFileName(performance.getFile().getEncodedFileName())
            .placeName(performance.getPlace().getName())
            .seats(book.getBookSeats().stream()
                .map(bs -> SeatMapper.toBookSeatViewDto(bs.getSeat()))
                .collect(Collectors.toList()))
            .quantity(book.getQuantity())
            .totalPrice(book.getTotalPrice())
            .bookStatus(book.getBookStatus())
            .build();
    }


    public static Page<BookedView> toBookedViewDtoList(Page<Book> books, String urlPrefix) {
        return books.map(book -> BookMapper.toBookedViewDto(book, urlPrefix));
    }

    public static BookScheduledTask toBookScheduledTaskEntity(Long bookId, LocalDateTime runtime) {
        return BookScheduledTask.create(bookId, runtime);
    }
}
