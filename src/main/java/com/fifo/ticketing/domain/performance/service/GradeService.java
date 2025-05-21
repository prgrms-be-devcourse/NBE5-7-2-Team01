package com.fifo.ticketing.domain.performance.service;

import com.fifo.ticketing.domain.performance.dto.GradeResponseDto;
import com.fifo.ticketing.domain.performance.mapper.GradeMapper;
import com.fifo.ticketing.domain.performance.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    public List<GradeResponseDto> getGradesByPlaceId(Long placeId) {
        return gradeRepository.findAllByPlaceId(placeId).stream()
                .map(GradeMapper::toDto)
                .toList();
    }
}
