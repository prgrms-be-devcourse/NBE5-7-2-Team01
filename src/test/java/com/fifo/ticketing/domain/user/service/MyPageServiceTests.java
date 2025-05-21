package com.fifo.ticketing.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fifo.ticketing.domain.like.repository.LikeRepository;
import com.fifo.ticketing.domain.performance.dto.LikedPerformanceDto;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.entity.Place;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.global.entity.File;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("ci")
@ExtendWith(MockitoExtension.class)
class MyPageServiceTests {

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private MyPageService myPageService;

    @Mock
    private Pageable pageable;

    private User user;
    private List<Performance> performanceList;
    private List<Performance> emptyPerformanceList = new ArrayList<>();
    private Performance performance1;
    private Performance performance2;
    private Place place;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("test").build();
        place = Place.builder().id(3L).name("공연장A").totalSeats(500).build();
        File file = File.builder().encodedFileName("qwe001.png").originalFileName("001.png")
            .build();
        performance1 = Performance.builder().id(1L).place(place).file(file).build();
        performance2 = Performance.builder().id(2L).place(place).file(file).build();
        performanceList = List.of(performance1, performance2);
    }

    @Test
    @DisplayName("찜한 목록이 있는 경우")
    void get_liked_performance_by_userId() {
        when(likeRepository.findLikedPerformancesByUserId(eq(user.getId()), any(Pageable.class)))
            .thenReturn(new PageImpl<>(performanceList));

        Page<LikedPerformanceDto> result = myPageService.getUserLikedPerformance(user.getId(),
            pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(performance1.getId());
        assertThat(result.getContent().get(1).getId()).isEqualTo(performance2.getId());
    }

    @Test
    @DisplayName("찜한 목록이 없는 경우")
    void no_liked_performance_by_userId() {
        when(likeRepository.findLikedPerformancesByUserId(eq(user.getId()), any(Pageable.class)))
            .thenReturn(new PageImpl<>(emptyPerformanceList));

        Page<LikedPerformanceDto> result = myPageService.getUserLikedPerformance(user.getId(),
            pageable);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("userId가 null인 경우 예외 발생")
    void get_liked_performance_userId_null() {
        ErrorException exception = assertThrows(ErrorException.class, () ->
            myPageService.getUserLikedPerformance(null, pageable)
        );

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_MEMBER);
    }
}
