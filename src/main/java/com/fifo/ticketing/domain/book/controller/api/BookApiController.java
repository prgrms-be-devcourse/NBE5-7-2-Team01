package com.fifo.ticketing.domain.book.controller.api;

import com.fifo.ticketing.domain.book.dto.BookCreateRequest;
import com.fifo.ticketing.domain.book.service.BookService;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/performances/{performanceId}/book")
public class BookApiController {

    private final BookService bookService;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;

//    @PostMapping
//    public ResponseEntity<Map<String, Long>> createBook(@PathVariable Long performanceId, @RequestParam Long userId, @RequestBody BookCreateRequest request) {
//        Long bookId = bookService.createBook(performanceId, userId, request);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(Map.of("bookId", bookId));
//    }

    @PostMapping
    public String createBook(
            @PathVariable Long performanceId,
            @RequestParam Long userId,
            @RequestParam List<Long> seatIds
    ) {
        BookCreateRequest request = new BookCreateRequest(seatIds);
        bookService.createBook(performanceId, userId, request);
        return "성공";
    }


}
