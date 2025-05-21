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
import java.nio.file.Files;
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
        validateImageType(file);
        String originalFileName = file.getOriginalFilename();
        String uuidFileName = generateUuidFileName(originalFileName);
        Path savePath = Paths.get(uploadDir, uuidFileName);
        try {
            file.transferTo(savePath.toFile());
            File generatedFile = File.builder()
                    .encodedFileName(uuidFileName)
                    .originalFileName(originalFileName)
                    .build();
            return fileRepository.save(generatedFile);
        } catch (Exception e) {
            handleFileUploadFailure(savePath);
            throw new ErrorException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void deleteFile(String encodedFileName) {
        if (encodedFileName != null && !encodedFileName.isEmpty()) {
            Path filePathToDelete = Paths.get(uploadDir, encodedFileName);
            try {
                if (Files.exists(filePathToDelete)) {
                    Files.delete(filePathToDelete);
                }
            } catch (IOException e) {
                throw new ErrorException(ErrorCode.FILE_DELETE_FAILED);
            }
        }
    }

    @Transactional
    public void updateFile(File existFile, MultipartFile file) throws IOException {
        validateImageType(file);
        String originalFileName = file.getOriginalFilename();
        String extension = getExtension(originalFileName);
        String uuidFileName = UUID.randomUUID() + "." + extension;
        Path savePath = Paths.get(uploadDir, uuidFileName);
        String oldEncodedFileName = null;
        try {
            if (existFile != null && existFile.getEncodedFileName() != null) {
                oldEncodedFileName = existFile.getEncodedFileName();
            }
            file.transferTo(savePath.toFile());
            if (oldEncodedFileName != null) {
                deleteFile(oldEncodedFileName);
            }
            if (existFile != null) {
                existFile.update(uuidFileName, originalFileName);
            } else {
                File newFile = File.builder()
                        .encodedFileName(uuidFileName)
                        .originalFileName(originalFileName)
                        .build();
                fileRepository.save(newFile);
            }
        } catch (Exception e) {
            handleFileUploadFailure(savePath);
            throw new ErrorException(ErrorCode.FILE_UPDATE_FAILED);
        }
    }

    public String generateUuidFileName(String originalFileName) {
        String extension = getExtension(originalFileName);
        return UUID.randomUUID() + "." + extension;
    }

    private void handleFileUploadFailure(Path savePath) {
        java.io.File targetFile = savePath.toFile();
        if (targetFile.exists()) {
            targetFile.delete();
        }
    }

    private static String getExtension(String originalFileName) {
        return originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf(".") + 1)
                : "";
    }

    private void validateImageType(MultipartFile file) {
        if (!ImageTypeChecker.isImage(file.getContentType()) || !ImageTypeChecker.validImageExtension(file)) {
            throw new ErrorException(ErrorCode.INVALID_IMAGE_TYPE);
        }
    }
}
