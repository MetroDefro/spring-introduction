package org.sparta.springintroduction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sparta.springintroduction.dto.ScheduleRequestDto;
import org.sparta.springintroduction.dto.ScheduleResponseDto;
import org.sparta.springintroduction.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/sprig-introduction")
@Tag(name = "Schedule Management ", description = "일정 관리 페이지 API")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedule")
    @Operation(summary = "일정 작성")
    @Parameters({
            @Parameter(name = "title", description = "제목(1~200자)", example = "제목입니다."),
            @Parameter(name = "contents", description = "내용(1~500자)", example = "내용입니다."),
            @Parameter(name = "charge", description = "담당자(email)", example = "damdang@email.com"),
            @Parameter(name = "password", description = "비밀번호(1~20자)", example = "1234"),
    })
    public ScheduleResponseDto createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.createSchedule(requestDto);
    }

    @GetMapping("/schedule")
    @Operation(summary = "선택한 일정 조회")
    public ScheduleResponseDto getSchedule(@RequestParam Long id) {
        return scheduleService.getSchedule(id);
    }

    @GetMapping("/schedules")
    @Operation(summary = "일정 목록 조회")
    public List<ScheduleResponseDto> getSchedules() {
        return scheduleService.getSchedules();
    }

    @PutMapping("/schedule")
    @Operation(summary = "선택한 일정 수정")
    @Parameters({
            @Parameter(name = "title", description = "제목(1~200자)", example = "제목입니다."),
            @Parameter(name = "contents", description = "내용(1~500자)", example = "내용입니다."),
            @Parameter(name = "charge", description = "담당자(email)", example = "damdang@email.com"),
            @Parameter(name = "password", description = "비밀번호(1~20자)", example = "1234"),
    })
    public ScheduleResponseDto updateSchedule(@RequestParam Long id, @Valid @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.updateSchedule(id, requestDto);
    }

    @DeleteMapping("/schedule")
    @Operation(summary = "선택한 일정 삭제")
    @Parameters({
            @Parameter(name = "password", description = "비밀번호(1~20자)", example = "1234"),
    })
    public Long deleteSchedule(@RequestParam Long id, @RequestBody Map<String, String> password) {
        return scheduleService.deleteSchedule(id, password.get("password"));
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }
}
