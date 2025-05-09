package com.fifo.ticketing.domain.performance.controller.api;

import com.fifo.ticketing.domain.performance.dto.GradeResponseDto;
import com.fifo.ticketing.domain.performance.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeApiController {
    private final GradeRepository gradeRepository;

    @GetMapping("/places/{placeId}")
    public List<GradeResponseDto> getGradesByPlace(@PathVariable Long placeId) {
        return gradeRepository.findAllByPlaceId(placeId).stream()
                .map(GradeResponseDto::from).toList();
    }
}
