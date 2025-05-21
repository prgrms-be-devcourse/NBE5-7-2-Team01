package com.fifo.ticketing.domain.like.entity;

import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.global.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "likes")
@NoArgsConstructor
public class Like extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_like_to_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false, foreignKey = @ForeignKey(name = "fk_like_to_performance"))
    private Performance performance;

    @Column(name = "is_liked")
    private boolean isLiked;

    @Builder
    public Like(Long id, User user, Performance performance, boolean isLiked) {
        this.id = id;
        this.user = user;
        this.performance = performance;
        this.isLiked = isLiked;
    }

    //이것만 세터 가능하게
    public void setLiked(boolean liked) {
        this.isLiked = liked;
    }
}
