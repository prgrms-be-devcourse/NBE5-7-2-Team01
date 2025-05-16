package com.fifo.ticketing.domain.book.repository;

import com.fifo.ticketing.domain.book.entity.BookSeat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookSeatRepository extends JpaRepository<BookSeat, Long> {

    List<BookSeat> findAllByBookId(Long bookId);

    @Query("SELECT bs FROM BookSeat bs JOIN FETCH bs.seat WHERE bs.book.id = :bookId")
    List<BookSeat> findAllByBookIdWithSeat(@Param("bookId") Long bookId);

}
