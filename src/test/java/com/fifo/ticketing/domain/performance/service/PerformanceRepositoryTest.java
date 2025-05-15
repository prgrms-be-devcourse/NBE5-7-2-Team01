package com.fifo.ticketing.domain.performance.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fifo.ticketing.domain.like.entity.LikeCount;
import com.fifo.ticketing.domain.like.repository.LikeCountRepository;
import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("ci")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=none",
    "spring.sql.init.mode=never"
})
@Sql(scripts = {"/schema.sql", "/data.sql"})
@SpringBootTest
@Transactional(readOnly = true)
class PerformanceRepositoryTest {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private LikeCountRepository likeCountRepository;

    private final Pageable pageable = PageRequest.of(0, 10);
    private final LocalDateTime now = LocalDateTime.of(2025, 5, 10, 0, 0);

    @Test
    @DisplayName("공연 목록 최신순 조회 테스트")
    void getPerformancesSortedByStartTime() {
        Page<Performance> result = performanceRepository.findUpcomingPerformancesOrderByStartTime(
            now, pageable);

        assertThat(result).isNotEmpty();

        List<Performance> performances = result.getContent();

        for (int i = 0; i < performances.size() - 1; i++) {
            Performance current = performances.get(i);
            Performance next = performances.get(i + 1);

            assertThat(current.getStartTime()).isAfter(now);

            int reservationCompare = current.getReservationStartTime()
                .compareTo(next.getReservationStartTime());

            if (reservationCompare == 0) {
                assertThat(current.getStartTime()).isBeforeOrEqualTo(next.getStartTime());
            } else {
                assertThat(current.getReservationStartTime()).isBefore(
                    next.getReservationStartTime());
            }
        }
    }

    @Test
    @DisplayName("공연 목록 좋아요순 조회 테스트 - 좋아요가 많은 순서대로 정렬된다")
    void getPerformancesSortedByLikesTest() {
        Page<Performance> result = performanceRepository.findUpcomingPerformancesOrderByLikes(now,
            pageable);

        assertThat(result).isNotEmpty();

        List<Performance> performances = result.getContent();

        for (int i = 0; i < performances.size() - 1; i++) {
            Performance current = performances.get(i);
            Performance next = performances.get(i + 1);

            Long currentLikeCount = likeCountRepository.findByPerformance(current)
                .map(LikeCount::getLikeCount).orElse(0L);
            Long nextLikeCount = likeCountRepository.findByPerformance(next)
                .map(LikeCount::getLikeCount).orElse(0L);

            assertThat(currentLikeCount).isGreaterThanOrEqualTo(nextLikeCount);
        }
    }

    @Test
    @DisplayName("공연 목록 조회 시, 사용자 지정 날짜 범위 내에 공연만 조회된다.")
    void getPerformancesByReservationPeriodTest() {
        LocalDateTime start = now;
        LocalDateTime end = LocalDateTime.of(2025, 6, 1, 0, 0);

        Page<Performance> result = performanceRepository.findUpcomingPerformancesByReservationPeriod(
            start, end, pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent())
            .allSatisfy(performance ->
                assertThat(performance.getStartTime()).isBetween(start, end));
    }

    @Test
    @DisplayName("공연 목록 조회 시, 해당 카테고리의 공연만 조회된다.")
    void getPerformancesByCategoryTest() {
        Page<Performance> result = performanceRepository.findUpcomingPerformancesByCategory(now,
            Category.CONCERT, pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).allMatch(p -> p.getCategory() == Category.CONCERT);
    }
}

