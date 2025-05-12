package com.fifo.ticketing.domain.book.repository;

import com.fifo.ticketing.domain.book.entity.BookSeat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookSeatRepository extends JpaRepository<BookSeat, Long> {

    List<BookSeat> findAllByBookId(Long bookId);

}
