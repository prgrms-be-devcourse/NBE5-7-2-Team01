package com.fifo.ticketing.domain.book.repository;

import com.fifo.ticketing.domain.book.dto.BookAdminDetailDto;
import com.fifo.ticketing.domain.book.dto.BookUserDetailDto;
import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.book.entity.BookStatus;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
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

    @Query(
        "SELECT new com.fifo.ticketing.domain.book.dto.BookAdminDetailDto(b.id, u.username, b.totalPrice, b.quantity) "
            + "FROM Book b "
            + "JOIN User u on b.user.id = u.id "
//            + "WHERE b.performance.id = :performanceId AND b.bookStatus IN ('CONFIRMED', 'PAYED')")
            + "WHERE b.performance.id = :performanceId")
    Page<BookAdminDetailDto> findAllBookDetailsAdmin(@Param("performanceId") Long id,
        Pageable pageable);

    @Query("SELECT new com.fifo.ticketing.domain.book.dto.BookUserDetailDto("
        + "b.id, p.id, p.title, b.totalPrice, b.quantity, u.username, f.encodedFileName, b.bookStatus) "
        + "FROM Book b "
        + "JOIN Performance p ON b.performance.id = p.id "
        + "JOIN User u ON b.user.id = u.id "
        + "LEFT JOIN File f ON p.file.id = f.id "
        + "WHERE b.id = :bookId AND p.id = :performanceId")
    BookUserDetailDto findBookDetailByBookId(@Param("bookId") Long bookId,
        @Param("performanceId") Long performanceId);

    boolean existsByUserAndPerformanceAndBookStatus(User user, Performance performance,
        BookStatus bookStatus);

}

