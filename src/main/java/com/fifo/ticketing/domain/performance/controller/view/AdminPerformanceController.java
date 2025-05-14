package com.fifo.ticketing.domain.performance.controller.view;


import com.fifo.ticketing.domain.book.dto.BookSeatViewDto;
import com.fifo.ticketing.domain.performance.dto.AdminPerformanceDetailResponse;
import com.fifo.ticketing.domain.performance.dto.AdminPerformanceResponseDto;
import com.fifo.ticketing.domain.performance.dto.PlaceResponseDto;
import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.service.PerformanceService;
import com.fifo.ticketing.domain.seat.service.SeatService;
import com.fifo.ticketing.domain.user.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/performances")
public class AdminPerformanceController {

    private final PerformanceService performanceService;
    private final SeatService seatService;

    @GetMapping
    public String viewPerformancesForAdmin(
            HttpSession session,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminPerformanceResponseDto> performances =
                performanceService.getPerformancesSortedByLatestForAdmin(pageable);
        String baseQuery = "?size=" + size;

        preparedModelAdmin(session, model, performances, page, baseQuery);
        return "admin/view_performances_admin";
    }

    @GetMapping(params = {"sort"})
    public String viewPerformancesSortedByForAdmin(
            HttpSession session,
            @RequestParam(value = "sort", defaultValue = "latest", required = false) String sort,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);

        Page<AdminPerformanceResponseDto> performances = switch (sort) {
            case "likes" -> performanceService.getPerformancesSortedByLikesForAdmin(pageable);
            default -> performanceService.getPerformancesSortedByLatestForAdmin(pageable);
        };
        String baseQuery = "?sort=" + sort + "&size=" + size;

        preparedModelAdmin(session, model, performances, page, baseQuery);
        return "admin/view_performances_admin";
    }

    @GetMapping(params = {"startDate", "endDate"})
    public String viewPerformancesWithinPeriodForAdmin(
            HttpSession session,
            @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminPerformanceResponseDto> performances = performanceService.getPerformancesByReservationPeriodForAdmin(
            startDate, endDate, pageable);
        String baseQuery = "?startDate=" + startDate + "&endDate=" + endDate + "&size=" + size;

        preparedModelAdmin(session, model, performances, page, baseQuery);
        return "admin/view_performances_admin";
    }

    @GetMapping(params = "category")
    public String viewPerformancesByCategoryForAdmin(
            HttpSession session,
            @RequestParam(value = "category") Category category,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminPerformanceResponseDto> performances = performanceService.getPerformancesByCategoryForAdmin(
            category,
            pageable);
        String baseQuery = "?category=" + category + "&size=" + size;

        preparedModelAdmin(session, model, performances, page, baseQuery);
        return "admin/view_performances_admin";
    }

    @GetMapping("/{performanceId}")
    public String getPerformanceDetail(
        @PathVariable Long performanceId,
        HttpSession session,
        Model model
    ) {
        SessionUser loginUser = (SessionUser) session.getAttribute("loginUser");

        AdminPerformanceDetailResponse performanceDetail = performanceService.getPerformanceDetailForAdmin(
            performanceId);

        List<BookSeatViewDto> seatViewDtos = seatService.getSeatsForPerformance(performanceId);

        model.addAttribute("performanceDetail", performanceDetail);
        model.addAttribute("performanceId", performanceId);
        model.addAttribute("userId", loginUser.id());
        model.addAttribute("seats", seatViewDtos);

        return "admin/performance_detail";
    }

    private void preparedModelAdmin(HttpSession session, Model model,
                               Page<AdminPerformanceResponseDto> performances, int page,
                               String baseQuery) {
        SessionUser loginUser = (SessionUser) session.getAttribute("loginUser");

        model.addAttribute("userId", loginUser.id());
        model.addAttribute("performances", performances.getContent());
        model.addAttribute("categories", Category.values());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", performances.getTotalPages());
        model.addAttribute("baseQuery", baseQuery);
    }

    @GetMapping("/create")
    public String createPerformance(Model model) {
        List<PlaceResponseDto> places = performanceService.getAllPlaces();
        model.addAttribute("places", places);
        return "admin/create_performance";
    }

    @GetMapping("/update/{performanceId}")
    public String updatePerformance(@PathVariable("performanceId") Long id, Model model) {
        AdminPerformanceDetailResponse performanceDetail = performanceService.getPerformanceDetailForAdmin(id);
        List<PlaceResponseDto> places = performanceService.getAllPlaces();
        model.addAttribute("performance", performanceDetail);
        model.addAttribute("places", places);
        return "admin/update_performance";
    }

}