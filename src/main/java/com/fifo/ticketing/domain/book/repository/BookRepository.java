package com.fifo.ticketing.domain.book.repository;

import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.user.entity.User;
import java.util.List;
import java.util.Optional;

import com.fifo.ticketing.domain.book.entity.BookStatus;
import com.fifo.ticketing.domain.performance.entity.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Optional<Book> findByUserIdAndId(Long userId, Long bookId);

    @Query("SELECT b FROM Book b "
            + "JOIN FETCH b.user "
            + "JOIN FETCH b.performance "
            + "WHERE b.performance = :performance AND b.bookStatus = :bookStatus")
    List<Book> findAllWithUserAndPerformanceByPerformanceAndBookStatus(
            @Param("performance") Performance performance,
            @Param("bookStatus") BookStatus bookStatus);

    @Query("SELECT b FROM Book b " +
        "WHERE b.user.id = :userId " +
        "AND b.bookStatus = :bookStatus " +
        "ORDER BY b.createdAt DESC")
    Page<Book> findAllByUserIdAndBookStatus(
        @Param("userId") Long userId,
        @Param("bookStatus") BookStatus bookStatus,
        Pageable pageable
    );

    @Query("SELECT b FROM Book b " +
        "WHERE b.user.id = :userId " +
        "AND b.performance.title LIKE %:performanceTitle% " +
        "AND b.bookStatus = :bookStatus " +
        "ORDER BY b.createdAt DESC")
    Page<Book> findAllByUserIdAndTitleAndBookStatus(
        @Param("userId") Long userId,
        @Param("performanceTitle") String performanceTitle,
        @Param("bookStatus") BookStatus bookStatus,
        Pageable pageable
    );

    @Query("SELECT b FROM Book b " +
        "WHERE b.user.id = :userId " +
        "AND b.performance.title LIKE %:performanceTitle% " +
        "ORDER BY b.createdAt DESC")
    Page<Book> findAllByUserIdAndTitle(
        @Param("userId") Long userId,
        @Param("performanceTitle") String performanceTitle,
        Pageable pageable
    );

    @Modifying
    @Query("UPDATE Book b SET b.bookStatus = :cancelStatus WHERE b.performance = :performance AND b.bookStatus = :currentStatus")
    void cancelAllByPerformance(@Param("performance") Performance performance,
            @Param("cancelStatus") BookStatus cancelStatus,
            @Param("currentStatus") BookStatus currentStatus);


  
    boolean existsByUserAndPerformanceAndBookStatus(User user, Performance performance, BookStatus bookStatus);

}

