package com.fifo.ticketing.domain.book.entity;

import com.fifo.ticketing.domain.seat.entity.Seat;
import com.fifo.ticketing.global.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@Table(name = "book_seats")
@NoArgsConstructor
@AllArgsConstructor
public class BookSeat extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_book_seat_to_book"))
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", foreignKey = @ForeignKey(name = "fk_book_seat_to_seat"))
    private Seat seat;

    public static BookSeat of(Book book, Seat seat) {
        return BookSeat.builder()
                .book(book)
                .seat(seat)
                .build();
    }
}

