package com.fifo.ticketing.domain.book.service;

import com.fifo.ticketing.domain.book.entity.BookScheduledTask;
import com.fifo.ticketing.domain.book.repository.BookScheduleRepository;
import com.fifo.ticketing.global.util.DateTimeUtil;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookScheduleInitializer {

    private final BookScheduleManager bookScheduleManager;
    private final BookScheduleRepository bookScheduleRepository;
    @Qualifier("taskScheduler")
    private final TaskScheduler taskScheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void reScheduleUnpaidBooks() {
        List<BookScheduledTask> pendingTasks = bookScheduleRepository.findAllPendingTasks();

        for (BookScheduledTask pendingTask : pendingTasks) {
            Date triggerTime = DateTimeUtil.toDate(pendingTask.getScheduledTime());
            taskScheduler.schedule(
                () -> bookScheduleManager.cancelIfUnpaid(pendingTask.getBookId()), triggerTime);
        }
    }

}
