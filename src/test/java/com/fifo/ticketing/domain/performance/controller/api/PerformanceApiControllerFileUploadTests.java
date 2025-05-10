package com.fifo.ticketing.domain.performance.controller.api;

import com.fifo.ticketing.domain.performance.entity.Grade;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.entity.Place;
import com.fifo.ticketing.domain.performance.repository.GradeRepository;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.performance.repository.PlaceRepository;
import com.fifo.ticketing.domain.seat.repository.SeatRepository;
import com.fifo.ticketing.global.entity.File;
import com.fifo.ticketing.global.repository.FileRepository;
import com.fifo.ticketing.global.util.ImageFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("ci")
public class PerformanceApiControllerFileUploadTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ImageFileService imageFileService;

    private Place savedPlace;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    private SeatRepository seatRepository;

    @BeforeEach
    void setUp() throws IOException {
        performanceRepository.deleteAll();
        seatRepository.deleteAll();
        fileRepository.deleteAll();

        Place place = Place.builder()
                .address("서울특별시 서초구 서초동 1307")
                .name("강남아트홀")
                .totalSeats(50)
                .build();
        savedPlace = placeRepository.save(place);

        Grade gradeS = Grade.builder()
                .place(savedPlace)
                .grade("S")
                .defaultPrice(120000)
                .seatCount(20)
                .build();

        Grade gradeA = Grade.builder()
                .place(savedPlace)
                .grade("A")
                .defaultPrice(90000)
                .seatCount(30)
                .build();

        gradeRepository.saveAll(List.of(gradeS, gradeA));

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    @DisplayName("공연 등록 시 실제 파일이 저장되고 데이터베이스에 반영되는지 확인 (001.png 사용)")
    @Test
    void test_performance_create_real_file_upload_001_png() throws Exception {
        // Given
        String requestJson = """
            {
                "title": "실제 파일 저장 공연 (png)",
                "description": "실제 파일 저장 테스트 (png)",
                "category": "CONCERT",
                "performanceStatus": true,
                "startTime": "2025-06-05T20:00:00",
                "endTime": "2025-06-05T22:00:00",
                "reservationStartTime": "2025-05-20T10:00:00",
                "placeId": %d
            }
            """.formatted(savedPlace.getId());

        ClassPathResource resource = new ClassPathResource("uploads/001.png"); // 사용할 이미지 파일 경로 변경
        InputStream inputStream = resource.getInputStream();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "001.png",
                MediaType.IMAGE_PNG_VALUE, // 사용할 이미지 Content-Type에 맞게 변경
                inputStream
        );

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
        );

        // When
        ResultActions resultActions = mockMvc.perform(multipart("/api/performances")
                        .file(file)
                        .file(request)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("공연이 등록되었습니다."));

        // Then
        // 1. 저장된 Performance 정보 확인
        Performance savedPerformance = performanceRepository.findByTitle("실제 파일 저장 공연 (png)");
        assertThat(savedPerformance).isNotNull();
        assertThat(savedPerformance.getFile()).isNotNull();

        // 2. 실제 파일 저장 경로에 파일이 존재하는지 확인
        Path storedFilePath = Paths.get(uploadDir, savedPerformance.getFile().getEncodedFileName());
        assertThat(Files.exists(storedFilePath)).isTrue();
        assertThat(Files.isRegularFile(storedFilePath)).isTrue();

        // 3. 데이터베이스에 File 정보가 정확하게 저장되었는지 확인
        File dbFile = fileRepository.findById(savedPerformance.getFile().getId()).orElse(null);
        assertThat(dbFile).isNotNull();
        assertThat(dbFile.getOriginalFileName()).isEqualTo("001.png"); // 원래 파일 이름 확인

        // [선택 사항] 저장된 파일 삭제 (테스트 후 정리)
        Files.deleteIfExists(storedFilePath);
    }
}