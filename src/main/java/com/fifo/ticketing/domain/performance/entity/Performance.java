package com.fifo.ticketing.domain.performance.entity;

import com.fifo.ticketing.domain.performance.dto.PerformanceRequestDto;
import com.fifo.ticketing.global.entity.BaseDateEntity;
import com.fifo.ticketing.global.entity.File;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private boolean deletedFlag;

    @Column(nullable = false)
    private LocalDateTime reservationStartTime;

    @Setter
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "file_id", foreignKey = @ForeignKey(name = "fk_performance_to_file"))
    private File file;


    public void update(PerformanceRequestDto dto, Place place) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.place = place;
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.category = dto.getCategory();
        this.performanceStatus = dto.isPerformanceStatus();
        this.reservationStartTime = dto.getReservationStartTime();
    }

    public void delete() {
        this.performanceStatus = false;
        this.deletedFlag = true;
    }
}
