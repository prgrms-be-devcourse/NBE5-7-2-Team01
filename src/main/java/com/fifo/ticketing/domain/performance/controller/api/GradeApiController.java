package com.fifo.ticketing.domain.performance.controller.api;

import com.fifo.ticketing.domain.performance.dto.GradeResponseDto;
import com.fifo.ticketing.domain.performance.service.GradeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeApiController {

    private final GradeService gradeService;

    @GetMapping("/places/{placeId}")
    public List<GradeResponseDto> getGradesByPlace(@PathVariable(value = "placeId") Long placeId) {
        return gradeService.getGradesByPlaceId(placeId);
    }
}
