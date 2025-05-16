package com.fifo.ticketing.domain.performance.service;

import static com.fifo.ticketing.global.exception.ErrorCode.FILE_UPLOAD_FAILED;
import static com.fifo.ticketing.global.exception.ErrorCode.INVALID_DELETED_PERFORMANCE;
import static com.fifo.ticketing.global.exception.ErrorCode.NOT_FOUND_GRADE;
import static com.fifo.ticketing.global.exception.ErrorCode.NOT_FOUND_PERFORMANCE;
import static com.fifo.ticketing.global.exception.ErrorCode.NOT_FOUND_PLACES;
import static com.fifo.ticketing.global.exception.ErrorCode.SEAT_CREATE_FAILED;

import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.book.service.BookService;
import com.fifo.ticketing.domain.like.entity.LikeCount;
import com.fifo.ticketing.domain.like.repository.LikeCountRepository;
import com.fifo.ticketing.domain.performance.dto.AdminPerformanceDetailResponse;
import com.fifo.ticketing.domain.performance.dto.AdminPerformanceResponseDto;
import com.fifo.ticketing.domain.performance.dto.PerformanceDetailResponse;
import com.fifo.ticketing.domain.performance.dto.PerformanceRequestDto;
import com.fifo.ticketing.domain.performance.dto.PerformanceResponseDto;
import com.fifo.ticketing.domain.performance.dto.PerformanceSeatGradeDto;
import com.fifo.ticketing.domain.performance.dto.PlaceResponseDto;
import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.entity.Grade;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.entity.Place;
import com.fifo.ticketing.domain.performance.mapper.PerformanceMapper;
import com.fifo.ticketing.domain.performance.mapper.PlaceMapper;
import com.fifo.ticketing.domain.performance.repository.GradeRepository;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.performance.repository.PlaceRepository;
import com.fifo.ticketing.domain.seat.entity.Seat;
import com.fifo.ticketing.domain.seat.service.SeatService;
import com.fifo.ticketing.global.event.PerformanceCanceledEvent;
import com.fifo.ticketing.global.entity.File;
import com.fifo.ticketing.global.exception.ErrorException;
import com.fifo.ticketing.global.util.ImageFileService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    @Value("${file.url-prefix}")
    private String urlPrefix;

    private final PlaceRepository placeRepository;
    private final PerformanceRepository performanceRepository;
    private final GradeRepository gradeRepository;
    private final SeatService seatService;
    private final ImageFileService imageFileService;
    private final LikeCountRepository likeCountRepository;
    private final BookService bookService;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional(readOnly = true)
    public PerformanceDetailResponse getPerformanceDetail(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
            .orElseThrow(() -> new ErrorException(NOT_FOUND_PERFORMANCE));

        List<Grade> grades = gradeRepository.findAllByPlaceId(performance.getPlace().getId());
        List<PerformanceSeatGradeDto> seatGrades = grades.stream()
            .map(PerformanceMapper::toSeatGradeDto)
            .toList();

        return PerformanceMapper.toDetailResponseDto(performance, seatGrades, urlPrefix);
    }

    @Transactional(readOnly = true)
    public AdminPerformanceDetailResponse getPerformanceDetailForAdmin(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
            .orElseThrow(() -> new ErrorException(NOT_FOUND_PERFORMANCE));
        List<Grade> grades = gradeRepository.findAllByPlaceId(performance.getPlace().getId());
        List<PerformanceSeatGradeDto> seatGrades = grades.stream()
            .map(PerformanceMapper::toSeatGradeDto)
            .toList();

        return PerformanceMapper.toAdminDetailResponseDto(performance, seatGrades);
    }

    public AdminPerformanceResponseDto getPerformanceUpdateForAdmin(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
            .orElseThrow(() -> new ErrorException(NOT_FOUND_PERFORMANCE));
        return PerformanceMapper.toAdminPerformanceResponseDto(performance, urlPrefix);
    }

    @Transactional(readOnly = true)
    public Page<PerformanceResponseDto> getPerformancesSortedByLatest(Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpcomingPerformancesOrderByStartTime(
            LocalDateTime.now(), pageable);
        return PerformanceMapper.toPagePerformanceResponseDto(performances, urlPrefix);
    }

    @Transactional(readOnly = true)
    public Page<AdminPerformanceResponseDto> getPerformancesSortedByLatestForAdmin(
        Pageable pageable) {
        Page<Performance> performances = performanceRepository
            .findUpcomingPerformancesOrderByReservationStartTimeForAdmin(pageable);
        return PerformanceMapper.toPageAdminPerformanceResponseDto(performances, urlPrefix);
    }


    @Transactional
    public Performance createPerformance(PerformanceRequestDto dto, MultipartFile file)
        throws IOException {
        // Place 조회 및 존재여부 확인
        Place place = findPlace(dto.getPlaceId());
        // Performance 생성 및 DB 저장
        Performance savedPerformance = savePerformance(dto, place);
        // File 업로드
        File uploadFile = uploadFile(file);
        // performance의 File을 Update
        savedPerformance.setFile(uploadFile);
        // Grade 조회
        List<Grade> grades = findGradesByPlace(place.getId());
        // Seat 목록 생성
        List<Seat> allSeats = generateSeatsForGrades(grades, savedPerformance);
        // Seats 저장 (Batch) - 100개 단위
        saveSeatsInBatch(allSeats);
        // LikeCount 저장
        saveLikeCount(savedPerformance);
        return savedPerformance;
    }

    @Transactional
    public Performance updatePerformance(Long id, PerformanceRequestDto dto, MultipartFile file)
        throws IOException {
        // 1. 수정을 위한 Performance 조회.
        Performance findPerformance = performanceRepository.findById(id).orElseThrow(
            () -> new ErrorException(NOT_FOUND_PERFORMANCE));

        // 추가. 삭제된 공연에 대해서 예외처리
        deletedPerformanceCheck(findPerformance);

        // 2. Place 조회
        Place newPlace = findPlace(dto.getPlaceId());

        // 3. 동일 장소인지 확인 후 수정 및 삭제
        if (!findPerformance.getPlace().getId().equals(dto.getPlaceId())) {
            // 좌석이 삭제되기 때문에 예약을 먼저 전부 취소하고, 메일도 전송해야 합니다.
            List<Book> books = bookService.cancelAllBook(findPerformance);
            // 해당 이벤트 자체는 Transaction의 커밋 이후에 이루어집니다.
            eventPublisher.publishEvent(new PerformanceCanceledEvent(books));

            // 기존 좌석 삭제 (soft or hard) -> 일단 soft라는 인식
            seatService.deleteSeatsByPerformanceId(id);

            // 새로운 장소 기준 등급 조회 및 좌석 재생성
            List<Grade> newGrades = findGradesByPlace(newPlace.getId());
            List<Seat> newSeats = generateSeatsForGrades(newGrades, findPerformance);
            saveSeatsInBatch(newSeats);

        }

        // 4. 공연 정보 수정
        findPerformance.update(dto, newPlace);

        // 5. 신규 파일이 업로드된 경우
        if (file != null && !file.isEmpty()) {
            // 기존 파일 정보가 있는 경우 파일 삭제
            File existFile = findPerformance.getFile();
            if (existFile != null) {
                imageFileService.updateFile(existFile, file);
            } else {
                // 5. 기존 파일 정보가 없는 경우에 새로 업로드된 경우
                File newFile = imageFileService.uploadFile(file);
                findPerformance.setFile(newFile);
            }
        }
        return findPerformance;
    }

    @Transactional
    public void deletePerformance(Long id) {
        // 1. 삭제를 위한 Performance 조회 (삭제되지 않은 파일만)
        Performance findPerformance = performanceRepository.findByIdAndDeletedFlagFalse(id)
            .orElseThrow(
                () -> new ErrorException(NOT_FOUND_PERFORMANCE));

        // 추가. 삭제된 공연에 대해서 예외처리
        deletedPerformanceCheck(findPerformance);

        // 2. 공연 삭제
        // 예약 삭제 / 좌석 삭제에서 영속성 컨텍스트가 초기화 되고, findPerformance가 flush 되지 않고 detach되는 문제 때문에 flush를 호출
        findPerformance.delete();
        performanceRepository.flush();

        // 3. 예약 삭제
        // books를 변수로 가져온 이유는, books의 유저를 기반으로 메일을 전송하기 위해서입니다.
        List<Book> books = bookService.cancelAllBook(findPerformance);

        // 4. 좌석 삭제
        // 좌석 삭제 시에 Query로 처리하는 부분 때문에 flush가 됩니다.
        seatService.deleteSeatsByPerformanceId(id);

        // 후속 절차로 메일 전송을 EventListener로 보낼 예정입니다.
        // 사용 변수는 books 입니다.
        eventPublisher.publishEvent(new PerformanceCanceledEvent(books));
    }

    private void deletedPerformanceCheck(Performance findPerformance) {
        if (findPerformance.isDeletedFlag()) {
            throw new ErrorException(INVALID_DELETED_PERFORMANCE);
        }
    }

    private void saveLikeCount(Performance savedPerformance) {
        likeCountRepository.save(LikeCount.builder()
            .likeCount(0L)
            .performance(savedPerformance)
            .build());
    }

    private Place findPlace(Long placeId) {
        return placeRepository.findById(placeId)
            .orElseThrow(() -> new ErrorException(NOT_FOUND_PLACES));
    }

    private Performance savePerformance(PerformanceRequestDto dto, Place place) {
        Performance performance = PerformanceMapper.toEntity(dto, place);
        return performanceRepository.save(performance);
    }

    private File uploadFile(MultipartFile file) {
        try {
            return imageFileService.uploadFile(file);
        } catch (IOException e) {
            throw new ErrorException(FILE_UPLOAD_FAILED);
        }
    }

    private List<Grade> findGradesByPlace(Long placeId) {
        List<Grade> grades = gradeRepository.findAllByPlaceId(placeId);
        if (grades.isEmpty()) {
            throw new ErrorException(NOT_FOUND_GRADE);
        }
        return grades;
    }

    private List<Seat> generateSeatsForGrades(List<Grade> grades, Performance performance) {
        List<Seat> allSeats = new ArrayList<>();
        for (Grade grade : grades) {
            for (int seatNumber = 1; seatNumber <= grade.getSeatCount(); seatNumber++) {
                allSeats.add(Seat.of(performance, grade, seatNumber));
            }
        }
        return allSeats;
    }

    private void saveSeatsInBatch(List<Seat> allSeats) {
        try {
            seatService.createSeats(allSeats);
        } catch (RuntimeException e) {
            throw new ErrorException(SEAT_CREATE_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public Page<PerformanceResponseDto> getPerformancesSortedByLikes(Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpcomingPerformancesOrderByLikes(
            LocalDateTime.now(), pageable);
        return PerformanceMapper.toPagePerformanceResponseDto(performances, urlPrefix);
    }

    @Transactional(readOnly = true)
    public Page<PerformanceResponseDto> getPerformancesByReservationPeriod(LocalDateTime start,
        LocalDateTime end, Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpcomingPerformancesByReservationPeriod(
            start, end, pageable);
        return PerformanceMapper.toPagePerformanceResponseDto(performances, urlPrefix);
    }

    @Transactional(readOnly = true)
    public Page<PerformanceResponseDto> getPerformancesByCategory(Category category,
        Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpcomingPerformancesByCategory(
            LocalDateTime.now(), category, pageable);
        return PerformanceMapper.toPagePerformanceResponseDto(performances, urlPrefix);
    }

    @Transactional(readOnly = true)
    public List<PlaceResponseDto> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        return places.stream()
            .map(PlaceMapper::toDtoForPerformanceCreate)
            .toList();
    }


    @Transactional(readOnly = true)
    public Page<AdminPerformanceResponseDto> getPerformancesSortedByLikesForAdmin(
        Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpcomingPerformancesOrderByLikesForAdmin(
            pageable);
        return PerformanceMapper.toPageAdminPerformanceResponseDto(performances, urlPrefix);
    }

    @Transactional(readOnly = true)
    public Page<AdminPerformanceResponseDto> getPerformancesByReservationPeriodForAdmin(
        LocalDateTime start,
        LocalDateTime end, Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpcomingPerformancesByReservationPeriodForAdmin(
            start, end, pageable);
        return PerformanceMapper.toPageAdminPerformanceResponseDto(performances, urlPrefix);
    }

    @Transactional(readOnly = true)
    public Page<AdminPerformanceResponseDto> getPerformancesByCategoryForAdmin(Category category,
        Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpcomingPerformancesByCategoryForAdmin(
            category, pageable);
        return PerformanceMapper.toPageAdminPerformanceResponseDto(performances, urlPrefix);
    }

    public Page<AdminPerformanceResponseDto> getPerformancesSortedByDeletedForAdmin(
        Pageable pageable) {
        Page<Performance> performances = performanceRepository.findUpComingPerformancesByDeletedFlagForAdmin(
            pageable);
        return PerformanceMapper.toPageAdminPerformanceResponseDto(performances, urlPrefix);
    }
}
