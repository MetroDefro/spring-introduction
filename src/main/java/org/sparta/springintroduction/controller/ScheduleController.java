package org.sparta.springintroduction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.springintroduction.dto.ScheduleRequestDto;
import org.sparta.springintroduction.dto.ScheduleResponseDto;
import org.sparta.springintroduction.security.UserDetailsImpl;
import org.sparta.springintroduction.service.FileService;
import org.sparta.springintroduction.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/sprig-introduction")
@Tag(name = "Schedule Management ", description = "일정 관리 페이지 API")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedule")
    @Operation(summary = "일정 작성")
    @Parameters({
            @Parameter(name = "title", description = "제목(1~200자)", example = "제목입니다."),
            @Parameter(name = "contents", description = "내용(1~500자)", example = "내용입니다."),
            @Parameter(name = "charge", description = "담당자(email)", example = "damdang@email.com"),
    })
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestPart ScheduleRequestDto requestDto,
                                                              @RequestPart(value = "file", required = false) MultipartFile file,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(scheduleService.createSchedule(requestDto, file, userDetails.getUser()));
    }

    @GetMapping("/schedule")
    @Operation(summary = "선택한 일정 조회")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@RequestParam Long id) {
        return ResponseEntity.ok(scheduleService.getSchedule(id));
    }

    @GetMapping("/schedules")
    @Operation(summary = "일정 목록 조회")
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules() {
        return ResponseEntity.ok(scheduleService.getSchedules());
    }

    @PutMapping("/schedule")
    @Operation(summary = "선택한 일정 수정")
    @Parameters({
            @Parameter(name = "title", description = "제목(1~200자)", example = "제목입니다."),
            @Parameter(name = "contents", description = "내용(1~500자)", example = "내용입니다."),
            @Parameter(name = "charge", description = "담당자(email)", example = "damdang@email.com"),
    })
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@RequestParam Long id,
                                                              @Valid @RequestPart ScheduleRequestDto requestDto,
                                                              @RequestPart(value = "file", required = false) MultipartFile file,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, requestDto, file, userDetails.getUser()));
    }

    @DeleteMapping("/schedule")
    @Operation(summary = "선택한 일정 삭제")
    public ResponseEntity<Long> deleteSchedule(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(scheduleService.deleteSchedule(id, userDetails.getUser()));
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }
}
