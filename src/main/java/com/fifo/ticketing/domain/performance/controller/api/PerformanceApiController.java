package com.fifo.ticketing.domain.performance.controller.api;

import com.fifo.ticketing.domain.performance.dto.PerformanceRequestDto;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.service.PerformanceService;
import com.fifo.ticketing.global.util.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
public class PerformanceApiController {

    private final PerformanceService performanceService;
    private final ImageFileService imageFileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPerformance(
            @RequestPart("file") MultipartFile file,
            @RequestPart("request") PerformanceRequestDto request) throws IOException {
        Performance performance = performanceService.createPerformance(request, file);
        return ResponseEntity.ok("공연이 등록되었습니다.");
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePerformance(
            @PathVariable Long id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("request") PerformanceRequestDto request) throws IOException {
        performanceService.updatePerformance(id, request, file);
        return ResponseEntity.ok("공연이 수정되었습니다.");
    }


}
