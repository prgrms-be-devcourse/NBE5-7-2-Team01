package com.fifo.ticketing.domain.seat.entity;

import com.fifo.ticketing.domain.performance.entity.Grade;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.global.entity.BaseDateEntity;
import com.fifo.ticketing.global.exception.AlertDetailException;
import com.fifo.ticketing.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Table(name = "seats")
@NoArgsConstructor
@BatchSize(size = 100)
public class Seat extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", foreignKey = @ForeignKey(name = "fk_seat_to_performance"))
    private Performance performance;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", foreignKey = @ForeignKey(name = "fk_seat_to_grade"))
    private Grade grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus seatStatus;

    @Version
    private Long version;

    public void book() {
        this.seatStatus = SeatStatus.BOOKED;
    }

    public void available() {
        this.seatStatus = SeatStatus.AVAILABLE;
    }

    public void occupy() {
        this.seatStatus = SeatStatus.OCCUPIED;
    }

    // Version의 경우는 JPA가 Persist 시에 자동으로 생성하기 때문에 생성자에 추가하지 않아도 됩니다!
    public Seat(Long id, Performance performance, String seatNumber, Integer price,
            Grade grade, SeatStatus seatStatus) {
        this.id = id;
        this.performance = performance;
        this.seatNumber = seatNumber;
        this.price = price;
        this.grade = grade;
        this.seatStatus = seatStatus;
    }

    public static Seat of(Performance performance, Grade grade, int number) {
        return new Seat(null, performance, grade.getGrade() + number, grade.getDefaultPrice(),
                grade, SeatStatus.AVAILABLE);
    }

    public void validateAvailable() {
        if (!this.getSeatStatus().equals(SeatStatus.AVAILABLE)) {
            throw new AlertDetailException(ErrorCode.SEAT_ALREADY_BOOKED,
                String.format("%d번 좌석은 이미 예약되었습니다.", this.id));
        }
    }
}
