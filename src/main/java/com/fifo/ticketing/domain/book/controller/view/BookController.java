package com.fifo.ticketing.domain.book.controller.view;

import com.fifo.ticketing.domain.book.dto.BookCreateRequest;
import com.fifo.ticketing.domain.book.service.BookService;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/performances/{performanceId}/book")
public class BookController {

    private final BookService bookService;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;

    @GetMapping("/complete/{bookId}")
    public String viewBookingComplete(@PathVariable Long performanceId, @PathVariable Long bookId,
        Model model) {



        return "book/complete";
    }
}
