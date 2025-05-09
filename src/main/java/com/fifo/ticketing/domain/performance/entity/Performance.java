package com.fifo.ticketing.domain.performance.entity;

import com.fifo.ticketing.global.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "performance")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Performance extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @MapsId("placeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", foreignKey = @ForeignKey(name = "fk_performance_to_place"))
    private Place place;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private boolean performanceStatus;

    @Column(nullable = false)
    private LocalDateTime reservationStartTime;


}
