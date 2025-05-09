package com.fifo.ticketing.domain.performance.controller.view;

import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.service.PerformanceService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    @GetMapping
    public void viewPerformances(
        @RequestParam(value = "page", defaultValue = "0", required = false) int page,
        @RequestParam(value = "size", defaultValue = "10", required = false) int size,
        Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Performance> performances = performanceService.getPerformancesSortedByLatest(pageable);

        preparedModel(model, performances, page);
    }

    @GetMapping(params = {"sort"})
    public void viewPerformancesSortedBy(
        @RequestParam(value = "sort", defaultValue = "latest", required = false) String sort,
        @RequestParam(value = "page", defaultValue = "0", required = false) int page,
        @RequestParam(value = "size", defaultValue = "10", required = false) int size,
        Model model) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Performance> performances = switch (sort) {
            case "likes" -> performanceService.getPerformancesSortedByLikes(pageable);
            default -> performanceService.getPerformancesSortedByLatest(pageable);
        };

        preparedModel(model, performances, page);
    }

    @GetMapping(params = {"startDate", "endDate"})
    public void viewPerformancesWithinPeriod(
        @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Performance> performances = performanceService.getPerformancesByReservationPeriod(
            startDate, endDate, pageable);

        preparedModel(model, performances, page);
    }

    private void preparedModel(Model model, Page<Performance> performances, int page) {
        model.addAttribute("performances", performances.getContent());
        model.addAttribute("categories", Category.values());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", performances.getTotalPages());
    }
}
