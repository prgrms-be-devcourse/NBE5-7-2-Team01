package com.fifo.ticketing.global.util;

import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.global.entity.File;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import com.fifo.ticketing.global.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRepository fileRepository;

    @Transactional
    public File uploadFile(MultipartFile file) throws IOException {
        if (!ImageTypeChecker.isImage(file.getContentType()) || !ImageTypeChecker.validImageExtension(file)) {
            throw new IllegalArgumentException("이미지 타입만 업로드 가능합니다.");
        }
        String originalFileName = file.getOriginalFilename();
        String extension = getExtension(originalFileName);
        String uuidFileName = UUID.randomUUID() + "." + extension;
        Path savePath = Paths.get(uploadDir, uuidFileName);
        try {
            file.transferTo(savePath.toFile());
            File generatedFile = new File(null, uuidFileName, originalFileName);
            return fileRepository.save(generatedFile);
        } catch (Exception e) {
            java.io.File targetFile = savePath.toFile();
            if (targetFile.exists()) {
                targetFile.delete();
            }
            throw new ErrorException(ErrorCode.FILE_UPLOAD_FAILED);
        }

    }

    private static String getExtension(String originalFileName) {
        return originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf(".") + 1)
                : "";
    }
}
