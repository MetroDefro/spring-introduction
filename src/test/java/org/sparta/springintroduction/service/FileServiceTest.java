package org.sparta.springintroduction.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparta.springintroduction.dto.ScheduleRequestDto;
import org.sparta.springintroduction.dto.ScheduleResponseDto;
import org.sparta.springintroduction.entity.Schedule;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService service;
    
    @DisplayName("파일 업로드 실패(이미지 파일이 아님)")
    @Test
    void fileUploadFail1() {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());
        assertThrows(IllegalArgumentException.class, () -> service.fileUpload(file));
    }

    @DisplayName("파일 업로드 실패(파일이 없음)")
    @Test
    void fileUploadFail2() {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "", "Hello World".getBytes());
        assertThrows(IllegalArgumentException.class, () -> service.fileUpload(file));
    }
}