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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("ci")
class PerformanceApiControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired

    @MockitoBean
    private ImageFileService imageFileService;

    private Place savedPlace;

    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    private SeatRepository seatRepository;

    @BeforeEach
    void setUp() throws IOException {

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
        when(imageFileService.uploadFile(any(MultipartFile.class))).thenReturn(File.builder().encodedFileName("test.webp").originalFileName("test.webp").build());
    }

    @DisplayName("@BeforeEach로 저장된 Place가 제대로 존재하는지 확인")
    @Test
    void test_setUp_place_exists() {
        Optional<Place> foundPlace = placeRepository.findById(savedPlace.getId());
        assertThat(foundPlace).isPresent();
        assertThat(foundPlace.get().getName()).isEqualTo("강남아트홀");
    }

    @DisplayName("@BeforeEach로 저장된 Grades가 제대로 존재하는지 확인")
    @Test
    void test_setUp_grades_exist() {
        List<Grade> foundGrades = gradeRepository.findAllByPlaceId(savedPlace.getId());
        assertThat(foundGrades).hasSize(2);
        assertThat(foundGrades.get(0).getGrade()).isEqualTo("S");
        assertThat(foundGrades.get(0).getPlace().getId()).isEqualTo(savedPlace.getId());
        assertThat(foundGrades.get(1).getGrade()).isEqualTo("A");
        assertThat(foundGrades.get(1).getPlace().getId()).isEqualTo(savedPlace.getId());
    }

    @DisplayName("H2 Database에 공연 등록이 성공하는 경우 (Mocking 사용)")
    @Test
    void test_performance_create_success_mocking() throws Exception {
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
                "placeId": %d
            }
            """.formatted(savedPlace.getId());

        ClassPathResource resource = new ClassPathResource("uploads/default.webp");
        InputStream inputStream = resource.getInputStream();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "default.webp",
                MediaType.IMAGE_JPEG_VALUE,
                inputStream
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

    @DisplayName("H2 Database에 공연 등록이 성공하는 경우 (cascade 설정 확인)")
    @Test
    void test_performance_create_success_cascade() throws Exception {
        // Given
        seatRepository.deleteAll();
        performanceRepository.deleteAll();

        String requestJson = """
        {
            "title": "라따뚜이",
            "description": "픽사의 명작 애니메이션",
            "category": "MOVIE",
            "performanceStatus": true,
            "startTime": "2025-06-01T19:00:00",
            "endTime": "2025-06-01T21:00:00",
            "reservationStartTime": "2025-05-12T19:00:00",
            "placeId": %d
        }
        """.formatted(savedPlace.getId());

        ClassPathResource resource = new ClassPathResource("uploads/default.webp");
        InputStream inputStream = resource.getInputStream();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "default.webp",
                MediaType.IMAGE_JPEG_VALUE,
                inputStream
        );

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
        );

        // ImageFileService Mocking (파일 업로드 성공 가정)
        File uploadedFile = File.builder().encodedFileName("encoded").originalFileName("default.webp").build();
        when(imageFileService.uploadFile(any(MultipartFile.class))).thenReturn(uploadedFile);

        // When & Then
        ResultActions resultActions = mockMvc.perform(multipart("/api/performances")
                        .file(file)
                        .file(request)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("공연이 등록되었습니다."));

        // 추가적인 검증: 저장된 Performance에 File이 연결되었는지 확인
        Performance savedPerformance = performanceRepository.findByTitle("라따뚜이");
        assertThat(savedPerformance).isNotNull();
        assertThat(savedPerformance.getFile()).isNotNull();
        assertThat(savedPerformance.getFile().getEncodedFileName()).isEqualTo("encoded");
    }
}