package com.fifo.ticketing.domain.book.controller;

import com.fifo.ticketing.domain.book.dto.BookCreateRequest;
import com.fifo.ticketing.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createBook(@RequestParam Long userId, @RequestBody BookCreateRequest request) {
        Long bookId = bookService.createBook(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("bookId", bookId));
    }
}
