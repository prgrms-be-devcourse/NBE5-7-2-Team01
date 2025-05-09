package com.fifo.ticketing.domain.book.entity;

import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.global.entity.BaseDateEntity;
import com.fifo.ticketing.global.entity.File;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookSeat> bookSeats = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private Integer quantity;
}

