package com.fifo.ticketing.domain.performance.controller.api;

import com.fifo.ticketing.domain.performance.entity.Place;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("ci")
@TestPropertySource(locations = "classpath:application-ci.yml")
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PerformanceApiControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaceRepository placeRepository;

    @BeforeEach
    void setUp() {
        Place place = Place.builder()
                .id(1L)
                .address("서울특별시 서초구 서초동 1307")
                .name("강남아트홀")
                .totalSeats(100)
                .build();
        placeRepository.save(place);


    }

//    @Test
//    @DisplayName("data.sql 실행 확인")
//    void test_data_sql_execution_check() {
//        Optional<Place> place = placeRepository.findById(2L);
//        assertThat(place).isPresent();
//        Place currentPlace = place.get();
//        assertThat(currentPlace.getAddress()).isEqualTo("서울특별시 마포구 와우산로21");
//    }



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