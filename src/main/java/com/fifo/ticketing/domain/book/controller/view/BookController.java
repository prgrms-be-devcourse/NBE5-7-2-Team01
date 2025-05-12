package com.fifo.ticketing.domain.book.controller.view;

import com.fifo.ticketing.domain.book.dto.BookCompleteDto;
import com.fifo.ticketing.domain.book.dto.BookCreateRequest;
import com.fifo.ticketing.domain.book.service.BookService;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/performances/{performanceId}/book")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public String createBook(
        @PathVariable Long performanceId,
        @RequestParam Long userId,
        @RequestParam List<Long> seatIds
    ) {
        BookCreateRequest request = new BookCreateRequest(seatIds);
        Long bookId = bookService.createBook(performanceId, userId, request);
        return "redirect:/performances/" + performanceId + "/book/complete/" + bookId;

    }

    @PostMapping("/complete/{bookId}/payment")
    public String completePayment(@PathVariable Long performanceId, @PathVariable Long bookId, RedirectAttributes redirectAttributes) {
        bookService.completePayment(bookId);

        redirectAttributes.addAttribute("paid", true);
        return "redirect:/performances/" + performanceId + "/book/complete/" + bookId;
    }

    @GetMapping("/complete/{bookId}")
    public String viewBookingComplete(@PathVariable Long performanceId,
        @PathVariable Long bookId,
        @RequestParam(value = "paid", required = false, defaultValue = "false") boolean paid,
        Model model) {

        BookCompleteDto bookCompleteInfo = bookService.getBookCompleteInfo(bookId);
        bookCompleteInfo.setPaymentCompleted(paid);

        model.addAttribute("book", bookCompleteInfo);
        model.addAttribute("bookId", bookId);

        return "book/complete";
    }
}
