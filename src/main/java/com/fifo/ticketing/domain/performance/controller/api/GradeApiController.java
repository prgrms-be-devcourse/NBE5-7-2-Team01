package com.fifo.ticketing.domain.performance.controller.api;

import com.fifo.ticketing.domain.performance.dto.GradeResponseDto;
import com.fifo.ticketing.domain.performance.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@Tag(name = "Admin_Grade", description = "관리자에 의한 좌석 등급 API")
public class GradeApiController {

    private final GradeService gradeService;

    @GetMapping("/places/{placeId}")
    @Operation(summary = "좌석 등급 조회", description = "공연장ID (placeId)를 기반으로 해당 공연장의 좌석 등급 목록을 조회합니다.")
    public List<GradeResponseDto> getGradesByPlace(@PathVariable(value = "placeId") Long placeId) {
        return gradeService.getGradesByPlaceId(placeId);
    }
}
