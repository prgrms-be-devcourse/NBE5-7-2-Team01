package com.fifo.ticketing.domain.seat.entity;

import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.global.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "seats", uniqueConstraints = @UniqueConstraint(columnNames = {"performance_id", "seat_number"}))
@NoArgsConstructor
@AllArgsConstructor
public class Seat extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", foreignKey = @ForeignKey(name = "fk_seat_performance_id"))
    private Performance performance;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus seatStatus;
}
