package com.fifo.ticketing.domain.book.controller.api;

import com.fifo.ticketing.domain.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/performances/book")
@RequiredArgsConstructor
@Tag(name = "Admin_Book", description = "관리자에 의한 예약 API")
public class BookAdminApiController {

    private final BookService bookService;

    @PostMapping("/cancel/{bookId}")
    @Operation(summary = "예약 취소", description = "예약ID(id)를 이용하여 예약 취소합니다.")
    public ResponseEntity<?> cancelPerformance(@PathVariable Long bookId) {
        bookService.cancelBookByAdmin(bookId);
        return ResponseEntity.ok("예약이 취소되었습니다.");
    }
}
