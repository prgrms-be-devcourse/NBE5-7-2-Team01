package com.fifo.ticketing.domain.performance.entity;

import com.fifo.ticketing.domain.performance.dto.PerformanceRequestDto;
import com.fifo.ticketing.global.entity.BaseDateEntity;
import com.fifo.ticketing.global.entity.File;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "performances")
@NoArgsConstructor
@AllArgsConstructor
public class Performance extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

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

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", foreignKey = @ForeignKey(name = "fk_performance_to_file"))
    private File file;

}
