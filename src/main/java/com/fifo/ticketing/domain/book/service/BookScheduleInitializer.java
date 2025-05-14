package com.fifo.ticketing.domain.book.service;

import com.fifo.ticketing.domain.book.entity.BookScheduledTask;
import com.fifo.ticketing.domain.book.mapper.BookMapper;
import com.fifo.ticketing.domain.book.repository.BookScheduleRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookScheduleInitializer {

    private final BookService bookService;
    private final BookScheduleRepository bookScheduleRepository;
    @Qualifier("taskScheduler")
    private final TaskScheduler taskScheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void reScheduleUnpaidBooks() {
        List<BookScheduledTask> pendingTasks = bookScheduleRepository.findAllPendingTasks();

        for (BookScheduledTask pendingTask : pendingTasks) {
            Date triggerTime = Date.from(pendingTask.getScheduledTime().atZone(ZoneId.systemDefault()).toInstant());
            taskScheduler.schedule(() -> bookService.cancelIfUnpaid(pendingTask.getBookId()), triggerTime);
        }
    }

}
