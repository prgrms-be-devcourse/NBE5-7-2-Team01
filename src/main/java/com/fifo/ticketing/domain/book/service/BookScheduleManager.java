package com.fifo.ticketing.domain.book.service;

import com.fifo.ticketing.domain.book.entity.BookScheduledTask;
import com.fifo.ticketing.domain.book.mapper.BookMapper;
import com.fifo.ticketing.domain.book.repository.BookScheduleRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookScheduleManager {

    private final BookCancelScheduleService bookCancelScheduleService;
    private final BookScheduleRepository bookScheduleRepository;
    @Qualifier("taskScheduler")
    private final TaskScheduler taskScheduler;


    @Transactional
    public void scheduleCancelTask(Long bookId, LocalDateTime runTime) {

        BookScheduledTask scheduledTask = bookScheduleRepository.save(
            BookMapper.toBookScheduledTaskEntity(bookId, runTime));

        Long taskId = scheduledTask.getId();

        Date triggerTime = Date.from(runTime.atZone(ZoneId.systemDefault()).toInstant());

        taskScheduler.schedule(() -> bookCancelScheduleService.cancelIfUnpaid(bookId,taskId), triggerTime);

    }

}
