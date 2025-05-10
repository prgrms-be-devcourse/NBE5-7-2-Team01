package com.fifo.ticketing.domain.performance.controller.api;

import com.fifo.ticketing.domain.performance.entity.Grade;
import com.fifo.ticketing.domain.performance.entity.Place;
import com.fifo.ticketing.domain.performance.repository.GradeRepository;
import com.fifo.ticketing.domain.performance.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("ci")
@TestPropertySource(locations = "classpath:application-ci.yml")
class PerformanceApiControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private GradeRepository gradeRepository;

    @BeforeEach
    void setUp() {
        Place place = Place.builder()
                .id(1L)
                .address("서울특별시 서초구 서초동 1307")
                .name("강남아트홀")
                .totalSeats(100)
                .build();
        placeRepository.save(place);

        // Grade 객체 생성
        Grade gradeS = Grade.builder()
                .id(1L)
                .place(place) // 해당 Place와 연결
                .grade("S")
                .defaultPrice(120000)
                .seatCount(20)
                .build();

        Grade gradeA = Grade.builder()
                .id(2L)
                .place(place) // 해당 Place와 연결
                .grade("A")
                .defaultPrice(90000)
                .seatCount(30)
                .build();

        Grade gradeB = Grade.builder()
                .id(3L)
                .place(place) // 해당 Place와 연결
                .grade("B")
                .defaultPrice(60000)
                .seatCount(30)
                .build();

        Grade gradeC = Grade.builder()
                .id(4L)
                .place(place) // 해당 Place와 연결
                .grade("C")
                .defaultPrice(40000)
                .seatCount(20)
                .build();

        // Grades 리스트에 저장
        List<Grade> grades = new ArrayList<>();
        grades.add(gradeS);
        grades.add(gradeA);
        grades.add(gradeB);
        grades.add(gradeC);

        // Grade 객체들 저장
        gradeRepository.saveAll(grades);
    }

    @DisplayName("H2 Database에 공연 등록이 성공하는 경우")
    @Test
    void test_performance_create_success() throws Exception {
        // Given
        String requestJson = """
            {
                "title": "라따뚜이",
                "description": "픽사의 명작 애니메이션",
                "category": "MOVIE",
                "performanceStatus": true,
                "startTime": "2025-06-01T19:00:00",
                "endTime": "2025-06-01T21:00:00",
                "reservationStartTime": "2025-05-12T19:00:00",
                "placeId": 1
            }
            """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image".getBytes()
        );

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/api/performances")
                        .file(file)
                        .file(request)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("공연이 등록되었습니다."));
    }
}