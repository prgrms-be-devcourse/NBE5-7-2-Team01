package com.fifo.ticketing.domain.book.controller;

import com.fifo.ticketing.domain.book.dto.BookCreateRequest;
import com.fifo.ticketing.domain.book.dto.BookSeatViewDto;
import com.fifo.ticketing.domain.book.service.BookService;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.entity.Place;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/performances/{performanceId}/book")
public class BookController {

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


    @GetMapping
    public String showSeatSelectPage(@PathVariable Long performanceId, @RequestParam Long userId, Model model) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_PERFORMANCE));

        int totalSeats = performance.getPlace().getTotalSeats();

        //TODO
        // 이 부분은 후에 SeatService로 분리하도록 하겠습니다.
        List<BookSeatViewDto> seatViewDtos = seatRepository.findAllByPerformanceId(performanceId)
                .stream()
                .map(seat -> new BookSeatViewDto(
                        seat.getId(),
                        seat.getSeatNumber(),
                        seat.getGrade(),
                        seat.getPrice(),
                        seat.getSeatStatus()
                ))
                .collect(Collectors.toList());

        model.addAttribute("performanceId", performanceId);
        model.addAttribute("userId", userId);
        model.addAttribute("totalSeats", totalSeats);
        model.addAttribute("seats", seatViewDtos);


        return "book/book";
    }

}
