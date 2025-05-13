package com.fifo.ticketing.domain.book.repository;

import com.fifo.ticketing.domain.book.entity.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByUserId(Long userId);

    Optional<Book> findByUserIdAndId(Long userId, Long bookId);
}
