package com.fifo.ticketing.domain.book.entity;

import com.fifo.ticketing.global.entity.BaseDateEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "book_scheduled_task")
@NoArgsConstructor
@AllArgsConstructor
public class BookScheduledTask extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;


}
