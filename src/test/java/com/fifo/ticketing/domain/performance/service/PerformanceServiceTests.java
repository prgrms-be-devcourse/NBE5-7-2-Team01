package com.fifo.ticketing.domain.performance.service;

import com.fifo.ticketing.domain.performance.dto.PerformanceRequestDto;
import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.entity.Grade;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.entity.Place;
import com.fifo.ticketing.domain.performance.mapper.PerformanceMapper;
import com.fifo.ticketing.domain.performance.repository.GradeRepository;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.performance.repository.PlaceRepository;
import com.fifo.ticketing.domain.seat.service.SeatService;
import com.fifo.ticketing.global.entity.File;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import com.fifo.ticketing.global.util.ImageFileService;
import org.hibernate.validator.internal.util.Contracts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("ci")
@ExtendWith(MockitoExtension.class)
class PerformanceServiceTests {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private SeatService seatService;

    @Mock
    private ImageFileService imageFileService;

    @InjectMocks
    private PerformanceService performanceService;

    @Mock
    private MultipartFile file;

    private Place place;
    private PerformanceRequestDto performanceRequestDto;

    @BeforeEach
    void setUp() {
        place = new Place(1L, "서울특별시 서초구 서초동 1307", "강남아트홀", 100);

        performanceRequestDto = new PerformanceRequestDto(
                "라따뚜이",
                "라따뚜이는 픽사의 영화입니다.",
                Category.MOVIE,
                false,
                LocalDateTime.of(2025, 6, 1, 19, 0),
                LocalDateTime.of(2025, 6, 1, 21, 0),
                LocalDateTime.of(2025, 5, 12, 19, 0),
                place.getId()
        );
    }

    @Test
    @DisplayName("공연 등록이 성공하는 경우")
    void test_create_performance_success() throws Exception {
        // Given
        when(placeRepository.findById(any(Long.class))).thenReturn(Optional.of(place));

        Performance performance = PerformanceMapper.toEntity(performanceRequestDto, place);
        when(performanceRepository.save(any(Performance.class))).thenReturn(performance);

        File uploadedFile = new File(null, "encoded-uuid.webp", "default.webp");
        when(imageFileService.uploadFile(file)).thenReturn(uploadedFile);

        Grade grade = new Grade(1L, place, "S", 20, 20000);
        when(gradeRepository.findAllByPlaceId(any(Long.class))).thenReturn(Arrays.asList(grade));

        doNothing().when(seatService).createSeats(anyList());

        // When
        Performance savePerformance = performanceService.createPerformance(performanceRequestDto, file);

        // Then
        assertThat(savePerformance).isNotNull();
        assertThat(savePerformance.getTitle()).isEqualTo(performanceRequestDto.getTitle());
        assertThat(savePerformance.getDescription()).isEqualTo(performanceRequestDto.getDescription());
        assertThat(savePerformance.getPlace()).isEqualTo(place);
        assertThat(savePerformance.getFile()).isEqualTo(uploadedFile);

        // Verify
        verify(placeRepository).findById(any(Long.class));
        verify(performanceRepository).save(any(Performance.class));
        verify(imageFileService).uploadFile(file);
        verify(gradeRepository).findAllByPlaceId(any(Long.class));
        verify(seatService).createSeats(anyList());
    }

    @Test
    @DisplayName("공연 등록 시 장소가 존재하지 않는 경우 예외 처리")
    void test_create_performance_not_found_place() throws Exception {
        // Given
        when(placeRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> performanceService.createPerformance(performanceRequestDto, file))
                .isInstanceOf(ErrorException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_PLACES);

        // Verify
        verify(placeRepository).findById(any(Long.class));
        verifyNoInteractions(performanceRepository, imageFileService, gradeRepository, seatService);
    }

    @Test
    @DisplayName("공연 등록 시 파일 업로드에 실패하는 경우 예외 처리")
    void test_create_performance_file_upload_failed() throws Exception {
        // Given
        when(placeRepository.findById(any(Long.class))).thenReturn(Optional.of(place));
        when(performanceRepository.save(any(Performance.class))).thenReturn(PerformanceMapper.toEntity(performanceRequestDto, place));
        when(imageFileService.uploadFile(file)).thenThrow(new IOException("파일 업로드 실패"));

        // When & Then
        assertThatThrownBy(() -> performanceService.createPerformance(performanceRequestDto, file))
                .isInstanceOf(ErrorException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FILE_UPLOAD_FAILED);

        // Verify : 파일 업로드는 호출, 좌석 생성은 호출되지 않습니다!
        verify(imageFileService).uploadFile(file);
        verifyNoMoreInteractions(seatService, gradeRepository);
    }

    @Test
    @DisplayName("공연 등록 시 등급이 존재하지 않는 경우 예외 처리")
    void test_create_performance_not_found_grade() throws Exception {
        // Given
        when(placeRepository.findById(any(Long.class))).thenReturn(Optional.of(place));
        when(performanceRepository.save(any(Performance.class))).thenReturn(PerformanceMapper.toEntity(performanceRequestDto, place));
        when(imageFileService.uploadFile(file)).thenReturn(new File(null, "encoded-uuid.webp", "default.webp"));
        when(gradeRepository.findAllByPlaceId(any(Long.class))).thenReturn(Arrays.asList());

        // When & Then
        assertThatThrownBy(() -> performanceService.createPerformance(performanceRequestDto, file))
                .isInstanceOf(ErrorException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_GRADE);

        // Verify : 등급 조회는 호출, 좌석 생성은 호출되지 않습니다!
        verify(gradeRepository).findAllByPlaceId(place.getId());
        verifyNoInteractions(seatService);
    }
    
    @Test
    @DisplayName("공연 등록 시 좌석 생성 중에 실패하는 경우 예외 처리")
    void test_create_performance_seat_create_failed() throws Exception {

        // Given
        when(placeRepository.findById(any(Long.class))).thenReturn(Optional.of(place));
        when(performanceRepository.save(any(Performance.class))).thenReturn(PerformanceMapper.toEntity(performanceRequestDto, place));
        when(imageFileService.uploadFile(file)).thenReturn(new File(null, "encoded-uuid.webp", "default.webp"));
        when(gradeRepository.findAllByPlaceId(any(Long.class))).thenReturn(Arrays.asList(new Grade(1L, place, "S", 10, 10000)));
        doThrow(new RuntimeException("Seat create failed")).when(seatService).createSeats(anyList());

        // When & Then
        assertThatThrownBy(() -> performanceService.createPerformance(performanceRequestDto, file))
                .isInstanceOf(ErrorException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SEAT_CREATE_FAILED);

        verify(seatService).createSeats(anyList());
        verifyNoMoreInteractions(seatService);
    }
}