package com.fifo.ticketing.domain.book.repository;

import com.fifo.ticketing.domain.book.entity.BookScheduledTask;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookScheduleRepository extends JpaRepository<BookScheduledTask, Long> {

    @Query("SELECT s FROM BookScheduledTask s WHERE s.taskStatus = 'PENDING'")
    List<BookScheduledTask> findAllPendingTasks();

}
