package com.fifo.ticketing.domain.performance.entity;

import com.fifo.ticketing.global.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "grades")
@NoArgsConstructor
@AllArgsConstructor
public class Grade extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", foreignKey = @ForeignKey(name = "fk_grade_to_place"))
    private Place place;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private Integer seatCount;

    @Column(nullable = false)
    private Integer defaultPrice;
}
