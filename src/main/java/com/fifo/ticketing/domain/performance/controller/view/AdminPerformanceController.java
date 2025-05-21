package com.fifo.ticketing.domain.performance.controller.view;

import com.fifo.ticketing.domain.book.dto.BookAdminDetailDto;
import com.fifo.ticketing.domain.book.dto.BookSeatViewDto;
import com.fifo.ticketing.domain.book.dto.BookUserDetailDto;
import com.fifo.ticketing.domain.book.service.BookService;
import com.fifo.ticketing.domain.performance.dto.AdminPerformanceBookDetailDto;
import com.fifo.ticketing.domain.performance.dto.AdminPerformanceDetailResponse;
import com.fifo.ticketing.domain.performance.dto.AdminPerformanceResponseDto;
import com.fifo.ticketing.domain.performance.dto.AdminPerformanceStaticsDto;
import com.fifo.ticketing.domain.performance.dto.PerformanceResponseDto;
import com.fifo.ticketing.domain.performance.dto.PlaceResponseDto;
import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.service.AdminPerformanceService;
import com.fifo.ticketing.domain.seat.service.SeatService;
import com.fifo.ticketing.domain.user.dto.SessionUser;
import com.fifo.ticketing.global.util.DateTimeValidator;
import com.fifo.ticketing.global.util.UserValidator;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/performances")
public class AdminPerformanceController {

    private final AdminPerformanceService adminPerformanceService;
    private final SeatService seatService;
    private final BookService bookService;

    @GetMapping
    public String viewPerformancesForAdmin(
            HttpSession session,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        return renderPerformanceList(
                session,
                model,
                adminPerformanceService.getPerformancesSortedByLatestForAdmin(pageable),
                page,
                "?size=" + size
        );
    }

    @GetMapping(params = {"search"})
    public String searchPerformances(
            HttpSession session,
            @RequestParam(value = "search", defaultValue = "", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);

        if (keyword == null || keyword.isEmpty()) {
            viewPerformancesForAdmin(session, page, size, model);
        }

        return renderPerformanceList(
                session,
                model,
                adminPerformanceService.searchPerformancesByKeyword(keyword, pageable),
                page,
                "?search=" + keyword + "&size=" + size
        );
    }


    @GetMapping(params = {"sort"})
    public String viewPerformancesSortedByForAdmin(
            HttpSession session,
            @RequestParam(value = "sort", defaultValue = "latest", required = false) String sort,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return renderPerformanceList(
                session,
                model,
                getPerformancesBySortForAdmin(sort, pageable),
                page,
                "?sort=" + sort + "&size=" + size
        );
    }

    private Page<AdminPerformanceResponseDto> getPerformancesBySortForAdmin(String sort,
            Pageable pageable) {
        return switch (sort) {
            case "likes" -> adminPerformanceService.getPerformancesSortedByLikesForAdmin(pageable);
            case "deleted" ->
                    adminPerformanceService.getPerformancesSortedByDeletedForAdmin(pageable);
            default -> adminPerformanceService.getPerformancesSortedByLatestForAdmin(pageable);
        };
    }

    @GetMapping(params = {"startDate", "endDate"})
    public String viewPerformancesWithinPeriodForAdmin(
            HttpSession session,
            @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model
    ) {
        DateTimeValidator.periodValidator(startDate, endDate);
        Pageable pageable = PageRequest.of(page, size);

        return renderPerformanceList(
                session,
                model,
                adminPerformanceService.getPerformancesByReservationPeriodForAdmin(startDate,
                        endDate, pageable),
                page,
                "?startDate=" + startDate + "&endDate=" + endDate + "&size=" + size
        );
    }

    @GetMapping(params = "category")
    public String viewPerformancesByCategoryForAdmin(
            HttpSession session,
            @RequestParam(value = "category") Category category,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return renderPerformanceList(
                session,
                model,
                adminPerformanceService.getPerformancesByCategoryForAdmin(category, pageable),
                page,
                "?category=" + category + "&size=" + size
        );
    }

    @GetMapping("/{performanceId}")
    public String getPerformanceDetailForAdmin(
            @PathVariable Long performanceId,
            HttpSession session,
            Model model
    ) {
        SessionUser loginUser = UserValidator.validateSessionUser(session);

        AdminPerformanceDetailResponse performanceDetail = adminPerformanceService.getPerformanceDetailForAdmin(
                performanceId);

        List<BookSeatViewDto> seatViewDtos = seatService.getSeatsForPerformance(performanceId);

        model.addAttribute("performanceDetail", performanceDetail);
        model.addAttribute("performanceId", performanceId);
        model.addAttribute("userId", loginUser.id());
        model.addAttribute("seats", seatViewDtos);
        model.addAttribute("showBackButton", true);

        return "admin/performance_detail_admin";
    }

    @GetMapping("/create")
    public String createPerformance(Model model) {
        List<PlaceResponseDto> places = adminPerformanceService.getAllPlaces();
        model.addAttribute("places", places);
        model.addAttribute("showBackButton", true);
        return "admin/create_performance";
    }

    @GetMapping("/update/{performanceId}")
    public String updatePerformance(@PathVariable("performanceId") Long id, Model model) {
        AdminPerformanceResponseDto performance = adminPerformanceService.getPerformanceUpdateForAdmin(
                id);
        List<PlaceResponseDto> places = adminPerformanceService.getAllPlaces();
        model.addAttribute("showBackButton", true);
        model.addAttribute("performance", performance);
        model.addAttribute("places", places);
        return "admin/update_performance";
    }

    @GetMapping("/chart")
    public String adminChart(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminPerformanceStaticsDto> statics = adminPerformanceService.getPerformanceStatics(
                pageable);
        model.addAttribute("stats", statics.getContent());
        model.addAttribute("currentPage", statics.getNumber());
        model.addAttribute("totalPages", statics.getTotalPages());
        return "admin/chart_admin";
    }

    @GetMapping("/book/{performanceId}")
    public String viewAdminPerformanceBookDetail(@PathVariable("performanceId") Long id,
            @PageableDefault(size = 5) Pageable pageable, Model model) {
        AdminPerformanceBookDetailDto performanceBookDetail = adminPerformanceService.getPerformanceBookDetail(
                id);
        Page<BookAdminDetailDto> bookAdminList = bookService.getBookAdminList(id, pageable);
        model.addAttribute("showBackButton", true);
        model.addAttribute("performanceBookDetail", performanceBookDetail);
        model.addAttribute("bookAdminListPage", bookAdminList);
        return "admin/performance_book_detail_admin";
    }

    @GetMapping("/book/{performanceId}/{bookId}")
    public String viewAdminPerformanceBookUserDetail(
            @PathVariable("performanceId") Long performanceId, @PathVariable("bookId") Long bookId,
            Model model) {
        BookUserDetailDto bookUserDetail = bookService.getBookUserDetail(bookId, performanceId);
        model.addAttribute("showBackButton", true);
        model.addAttribute("bookUserDetail", bookUserDetail);
        return "admin/performance_book_user_detail_admin";
    }

    private String renderPerformanceList(
            HttpSession session,
            Model model,
            Page<AdminPerformanceResponseDto> performances,
            int page,
            String queryParams
    ) {
        SessionUser loginUser = UserValidator.validateSessionUser(session);
        Long userId = loginUser.id();

        model.addAttribute("userId", userId);
        model.addAttribute("performances", performances.getContent());
        model.addAttribute("categories", Category.values());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", performances.getTotalPages());
        model.addAttribute("baseQuery", queryParams);

        return "/admin/view_performances_admin";
    }

}