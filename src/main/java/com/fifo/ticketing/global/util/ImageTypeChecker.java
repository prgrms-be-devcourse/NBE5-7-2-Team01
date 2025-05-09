package com.fifo.ticketing.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageTypeChecker {

    static public boolean isImage(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    static public boolean validImageExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return filename != null && (
                filename.toLowerCase().endsWith(".jpg") ||
                filename.toLowerCase().endsWith(".jpeg") ||
                filename.toLowerCase().endsWith(".png") ||
                filename.toLowerCase().endsWith(".gif") ||
                filename.toLowerCase().endsWith(".webp")
        );
    }
}
