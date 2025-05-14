package com.fifo.ticketing.domain.user.controller;

import com.fifo.ticketing.domain.performance.dto.LikedPerformanceDto;
import com.fifo.ticketing.domain.user.dto.SessionUser;
import com.fifo.ticketing.domain.user.service.MyPageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final MyPageService myPageService;

    @GetMapping("/users/likes")
    public Page<LikedPerformanceDto> getUserLikes(HttpSession session,
        @RequestParam(value = "page", defaultValue = "0", required = false) int page,
        @RequestParam(value = "size", defaultValue = "5", required = false) int size) {
        Pageable pageable = PageRequest.of(page, size);
        SessionUser loginUser = (SessionUser) session.getAttribute("loginUser");
        Long userId = loginUser.id();
        return myPageService.getUserLikedPerformance(
            userId, pageable
        );
    }
}
