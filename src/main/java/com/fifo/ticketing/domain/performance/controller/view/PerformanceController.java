package com.fifo.ticketing.domain.performance.controller.view;


import com.fifo.ticketing.domain.book.dto.BookSeatViewDto;
import com.fifo.ticketing.domain.like.service.LikeService;
import com.fifo.ticketing.domain.performance.dto.PerformanceDetailResponse;
import com.fifo.ticketing.domain.performance.dto.PerformanceResponseDto;
import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.service.PerformanceService;
import com.fifo.ticketing.domain.seat.service.SeatService;
import com.fifo.ticketing.domain.user.dto.SessionUser;
import com.fifo.ticketing.global.util.DateTimeValidator;
import com.fifo.ticketing.global.util.UserValidator;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceService performanceService;
    private final SeatService seatService;
    private final LikeService likeService;

    @GetMapping
    public String viewPerformances(
        HttpSession session,
        @RequestParam(value = "page", defaultValue = "0", required = false) int page,
        @RequestParam(value = "size", defaultValue = "10", required = false) int size,
        Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PerformanceResponseDto> performances = performanceService.getPerformancesSortedByLatest(
            pageable);
        String baseQuery = "?size=" + size;

        preparedModel(session, model, performances, page, baseQuery);
        return "/performance/view_performances";
    }

    @GetMapping(params = {"sort"})
    public String viewPerformancesSortedBy(
        HttpSession session,
        @RequestParam(value = "sort", defaultValue = "latest", required = false) String sort,
        @RequestParam(value = "page", defaultValue = "0", required = false) int page,
        @RequestParam(value = "size", defaultValue = "10", required = false) int size,
        Model model) {
        Pageable pageable = PageRequest.of(page, size);

        Page<PerformanceResponseDto> performances = switch (sort) {
            case "likes" -> performanceService.getPerformancesSortedByLikes(pageable);
            default -> performanceService.getPerformancesSortedByLatest(pageable);
        };
        String baseQuery = "?sort=" + sort + "&size=" + size;

        preparedModel(session, model, performances, page, baseQuery);
        return "/performance/view_performances";
    }

    @GetMapping(params = {"startDate", "endDate"})
    public String viewPerformancesWithinPeriod(
        HttpSession session,
        @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        Model model
    ) {
        DateTimeValidator.periodValidator(startDate, endDate);

        Pageable pageable = PageRequest.of(page, size);
        Page<PerformanceResponseDto> performances = performanceService.getPerformancesByReservationPeriod(
            startDate, endDate, pageable);
        String baseQuery = "?startDate=" + startDate + "&endDate=" + endDate + "&size=" + size;

        preparedModel(session, model, performances, page, baseQuery);
        return "/performance/view_performances";
    }

    @GetMapping(params = "category")
    public String viewPerformancesByCategory(HttpSession session,
        @RequestParam(value = "category") Category category,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PerformanceResponseDto> performances = performanceService.getPerformancesByCategory(
            category,
            pageable);
        String baseQuery = "?category=" + category + "&size=" + size;

        preparedModel(session, model, performances, page, baseQuery);
        return "/performance/view_performances";
    }

    @GetMapping("/{performanceId}")
    public String getPerformanceDetail(
        @PathVariable Long performanceId,
        HttpSession session,
        Model model
    ) {
        SessionUser loginUser = UserValidator.validateSessionUser(session);

        PerformanceDetailResponse performanceDetail = performanceService.getPerformanceDetail(
            performanceId);

        List<BookSeatViewDto> seatViewDtos = seatService.getSeatsForPerformance(performanceId);

        model.addAttribute("performanceDetail", performanceDetail);
        model.addAttribute("performanceId", performanceId);
        model.addAttribute("userId", loginUser.id());
        model.addAttribute("seats", seatViewDtos);

        return "performance/detail";
    }

    private void preparedModel(HttpSession session, Model model,
        Page<PerformanceResponseDto> performances, int page,
        String baseQuery) {
        SessionUser loginUser = UserValidator.validateSessionUser(session);

        Long userId = loginUser.id();
        List<Long> likedPerformancesIds = likeService.getLikedPerformancesIds(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("performances", performances.getContent());
        model.addAttribute("categories", Category.values());
        model.addAttribute("likedPerformanceIds", likedPerformancesIds);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", performances.getTotalPages());
        model.addAttribute("baseQuery", baseQuery);
    }
}