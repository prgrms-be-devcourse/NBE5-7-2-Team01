package com.fifo.ticketing.domain.performance.service;

import static com.fifo.ticketing.global.exception.ErrorCode.*;

import com.fifo.ticketing.domain.performance.dto.PerformanceRequestDto;
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
import com.fifo.ticketing.global.entity.File;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fifo.ticketing.global.util.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PlaceRepository placeRepository;
    private final PerformanceRepository performanceRepository;
    private final GradeRepository gradeRepository;

    private final SeatService seatService;
    private final ImageFileService imageFileService;

    @Transactional(readOnly = true)
    public Page<Performance> getPerformancesSortedByLatest(Pageable pageable) {
        Page<Performance> upcomingPerformances = performanceRepository.findUpcomingPerformancesOrderByReservationStartTime(
            LocalDateTime.now(), pageable);
        if (upcomingPerformances.isEmpty()) {
            throw new ErrorException(NOT_FOUND_PERFORMANCES);
        }
        return upcomingPerformances;
    }
    @Transactional
    public Performance createPerformance(PerformanceRequestDto dto, MultipartFile file) throws IOException {
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

        return savedPerformance;
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
    public Page<Performance> getPerformancesSortedByLikes(Pageable pageable) {
        Page<Performance> upcomingPerformances = performanceRepository.findUpcomingPerformancesOrderByLikes(
            LocalDateTime.now(), pageable);
        if (upcomingPerformances.isEmpty()) {
            throw new ErrorException(NOT_FOUND_PERFORMANCES);
        }
        return upcomingPerformances;
    }

    @Transactional(readOnly = true)
    public Page<Performance> getPerformancesByReservationPeriod(LocalDateTime start,
        LocalDateTime end, Pageable pageable) {
        Page<Performance> upcomingPerformances = performanceRepository.findUpcomingPerformancesByReservationPeriod(
            start, end, pageable);
        if (upcomingPerformances.isEmpty()) {
            throw new ErrorException(NOT_FOUND_PERFORMANCES);
        }
        return upcomingPerformances;
    }

    @Transactional(readOnly = true)
    public Page<Performance> getPerformancesByCategory(Category category, Pageable pageable) {
        Page<Performance> upcomingPerformances = performanceRepository.findUpcomingPerformancesByCategory(
            LocalDateTime.now(), category, pageable);
        if (upcomingPerformances.isEmpty()) {
            throw new ErrorException(NOT_FOUND_PERFORMANCES);
        }
        return upcomingPerformances;
    }

    @Transactional(readOnly = true)
    public List<PlaceResponseDto> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        return places.stream()
                .map(PlaceMapper::toDtoForPerformanceCreate)
                .toList();
    }
}
