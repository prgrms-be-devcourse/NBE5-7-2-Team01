package com.fifo.ticketing.domain.book.repository;

import com.fifo.ticketing.domain.book.entity.BookScheduledTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookScheduleRepository extends JpaRepository<BookScheduledTask, Long> {

}
