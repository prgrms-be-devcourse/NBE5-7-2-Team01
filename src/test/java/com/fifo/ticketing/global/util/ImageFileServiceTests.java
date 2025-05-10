//package com.fifo.ticketing.global.util;
//
//import com.fifo.ticketing.global.entity.File;
//import com.fifo.ticketing.global.repository.FileRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class ImageFileServiceTests {
//
//    @Mock
//    private FileRepository fileRepository;
//
//    @Mock
//    private MultipartFile mockFile;
//
//    @InjectMocks
//    private ImageFileService imageFileService;
//
//
//    @BeforeEach
//    void setUp() {
//        ReflectionTestUtils.setField(imageFileService, "uploadDir", "/test/uploads");
//    }
//
//    @Test
//    @DisplayName("파일이 정상적으로 업로드 되는 경우")
//    @Transactional
//    void test_upload_file_success() throws IOException {
//        // Given
//        when(mockFile.getContentType()).thenReturn("image/png");
//        when(mockFile.getOriginalFilename()).thenReturn("test.png");
//        doNothing().when(mockFile).transferTo(any(java.io.File.class));
//        File expectedFile = new File(null, "generated-uuid.png", "test.png");
//        when(fileRepository.save(any(File.class))).thenReturn(expectedFile);  // 반드시 추가!
//
//        // When
//        File result = imageFileService.uploadFile(mockFile);
//        assertThat(result).isNotNull();  // Null 체크
//
//        // 4. 저장된 객체 검증
//        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
//        verify(fileRepository).save(fileCaptor.capture());
//        File savedFile = fileCaptor.getValue();
//
//        // 5. 필드별 검증
//        assertThat(savedFile.getOriginalFileName()).isEqualTo("test.png");
//        assertThat(result.getOriginalFileName()).isEqualTo(savedFile.getOriginalFileName());
//
//        // 6. 확장자 검증 (안전한 방법)
//        String fileName = savedFile.getEncodedFileName();
//        assertThat(fileName).endsWith(".png");  // 확장자만 확인
//    }
//
//    @Test
//    @DisplayName("이미지 확장자가 아닌 경우 예외 발생")
//    void test_invalid_file_type_throws_exception() throws Exception {
//        when(mockFile.getContentType()).thenReturn("text/plain");
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            imageFileService.uploadFile(mockFile);
//        });
//
//        assertThat(exception.getMessage()).isEqualTo("이미지 타입만 업로드 가능합니다.");
//    }
//
//}