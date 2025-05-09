package com.fifo.ticketing.domain.performance.controller.api;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("ci")
class PerformanceApiControllerTests {
    @Autowired
    private MockMvc mockMvc;

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