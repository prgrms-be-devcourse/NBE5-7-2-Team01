package com.fifo.ticketing.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.url-prefix}")
    private String urlPrefix;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 운영체제 경로 정규화
        String normalizedPath = Paths.get(uploadDir).normalize().toString();
        // Windows 경로 처리 (C:\ → /C:/)
        if (normalizedPath.startsWith("\\")) {
            normalizedPath = normalizedPath.substring(1);
        }
        // URI 형식으로 변환
        String externalPath = "file:" + normalizedPath.replace("\\", "/");
        // 경로 끝에 슬래시가 없으면 추가
        if (!externalPath.endsWith("/")) {
            externalPath += "/";
        }
        registry.addResourceHandler(urlPrefix + "**")
                .addResourceLocations(externalPath);
    }
}
