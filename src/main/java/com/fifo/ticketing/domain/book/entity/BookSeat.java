package com.fifo.ticketing.domain.book.entity;

import com.fifo.ticketing.domain.seat.entity.Seat;
import com.fifo.ticketing.global.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
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
}
