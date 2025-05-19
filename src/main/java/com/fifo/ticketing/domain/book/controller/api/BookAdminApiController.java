package com.fifo.ticketing.domain.book.controller.api;

import com.fifo.ticketing.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/performances/book")
@RequiredArgsConstructor
public class BookAdminApiController {

    private final BookService bookService;

    @PostMapping("/cancel/{bookId}")
    public ResponseEntity<?> cancelPerformance(@PathVariable Long bookId) {
        bookService.cancelBookByAdmin(bookId);
        return ResponseEntity.ok("예약이 취소되었습니다.");
    }
}
