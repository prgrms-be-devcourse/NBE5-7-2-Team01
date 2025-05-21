package com.fifo.ticketing.domain.like.entity;

import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.global.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "like_count")
@NoArgsConstructor
@AllArgsConstructor
public class LikeCount extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "performance_id", nullable = false, foreignKey = @ForeignKey(name = "fk_like_count_performance"))
    private Performance performance;

    @Column(nullable = false)
    private Long likeCount;

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

}
