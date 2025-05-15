package com.fifo.ticketing.domain.book.repository;

import com.fifo.ticketing.domain.book.entity.Book;
import java.util.List;
import java.util.Optional;

import com.fifo.ticketing.domain.book.entity.BookStatus;
import com.fifo.ticketing.domain.performance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByUserId(Long userId);

    Optional<Book> findByUserIdAndId(Long userId, Long bookId);

    @Query("SELECT b FROM Book b "
            + "JOIN FETCH b.user "
            + "JOIN FETCH b.performance "
            + "WHERE b.performance = :performance AND b.bookStatus = :bookStatus")
    List<Book> findAllWithUserAndPerformanceByPerformanceAndBookStatus(
            @Param("performance") Performance performance,
            @Param("bookStatus") BookStatus bookStatus);

    @Modifying
    @Query("UPDATE Book b SET b.bookStatus = :cancelStatus WHERE b.performance = :performance AND b.bookStatus = :currentStatus")
    void cancelAllByPerformance(@Param("performance") Performance performance,
            @Param("cancelStatus") BookStatus cancelStatus,
            @Param("currentStatus") BookStatus currentStatus);
}
