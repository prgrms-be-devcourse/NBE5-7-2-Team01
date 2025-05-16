package com.fifo.ticketing.domain.book.mapper;

import com.fifo.ticketing.domain.book.dto.BookCompleteDto;
import com.fifo.ticketing.domain.book.dto.BookMailSendDto;
import com.fifo.ticketing.domain.book.dto.BookedView;
import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.book.entity.BookScheduledTask;
import com.fifo.ticketing.domain.book.entity.BookSeat;
import com.fifo.ticketing.domain.book.entity.BookStatus;
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

    public static Book toBookEntity(User user, Performance performance, int totalPrice,
        int quantity) {
        return Book.create(user, performance, totalPrice, quantity);
    }

    public static List<BookSeat> toBookSeatEntities(Book book, List<Seat> seats) {
        return seats.stream()
            .map(seat -> BookSeat.of(book, seat))
            .collect(Collectors.toList());
    }

    public static BookCompleteDto toBookCompleteDto(Book book, String urlPrefix) {
        return BookCompleteDto.builder()
            .performanceId(book.getPerformance().getId())
            .performanceTitle(book.getPerformance().getTitle())
            .performanceStartTime(book.getPerformance().getStartTime())
            .performanceEndTime(book.getPerformance().getEndTime())
            .placeName(book.getPerformance().getPlace().getName())
            .encodedFileName(book.getPerformance().getFile().getEncodedFileName())
            .seats(book.getBookSeats().stream()
                .map(bs -> SeatMapper.toBookSeatViewDto(bs.getSeat()))
                .collect(Collectors.toList()))
            .totalPrice(book.getTotalPrice())
            .quantity(book.getQuantity())
            .paymentCompleted(false)
            .urlPrefix(urlPrefix)
            .build();
    }

    public static BookedView toBookedViewDto(Book book, String urlPrefix) {
        Performance performance = book.getPerformance();

        return BookedView.builder()
            .bookId(book.getId())
            .performanceId(performance.getId())
            .performanceTitle(performance.getTitle())
            .placeName(performance.getPlace().getName())
            .encodedFileName(performance.getFile().getEncodedFileName())
            .seats(book.getBookSeats().stream()
                .map(bs -> SeatMapper.toBookSeatViewDto(bs.getSeat()))
                .collect(Collectors.toList()))
            .quantity(book.getQuantity())
            .totalPrice(book.getTotalPrice())
            .bookStatus(book.getBookStatus())
            .urlPrefix(urlPrefix)
            .build();
    }

    public static Page<BookedView> toBookedViewDtoList(Page<Book> books, String urlPrefix) {
        return books.map(book -> BookMapper.toBookedViewDto(book, urlPrefix));
    }

    public static BookScheduledTask toBookScheduledTaskEntity(Long bookId, LocalDateTime runtime) {
        return BookScheduledTask.create(bookId, runtime);
    }

    public static BookMailSendDto getBookMailInfo(Book book, String urlPrefix) {
        Performance performance = book.getPerformance();
        User user = book.getUser();

        BookStatus status = book.getBookStatus();

        String mailTitle = switch (status) {
            case PAYED -> performance.getTitle() + " 예매가 확정되었습니다";
            case CANCELED -> performance.getTitle() + " 예매가 취소되었습니다";
            default -> "예매 상태 안내";
        };

        return BookMailSendDto.builder()
            .emailAddr(user.getEmail())
            .title(mailTitle)
            .performanceId(performance.getId())
            .performanceTitle(performance.getTitle())
            .performanceStartTime(performance.getStartTime())
            .performanceEndTime(performance.getEndTime())
            .placeName(performance.getPlace().getName())
            .seats(book.getBookSeats().stream()
                .map(bs -> SeatMapper.toBookSeatViewDto(bs.getSeat()))
                .collect(Collectors.toList()))
            .totalPrice(book.getTotalPrice())
            .quantity(book.getQuantity())
            .bookStatus(book.getBookStatus())
            .urlPrefix(urlPrefix)
            .build();
    }
}
