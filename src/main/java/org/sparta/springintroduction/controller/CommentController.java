package org.sparta.springintroduction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sparta.springintroduction.dto.CommentRequestDto;
import org.sparta.springintroduction.dto.CommentResponseDto;
import org.sparta.springintroduction.dto.ScheduleRequestDto;
import org.sparta.springintroduction.dto.ScheduleResponseDto;
import org.sparta.springintroduction.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/sprig-introduction")
@Tag(name = "Comment", description = "댓글 API")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    @Operation(summary = "댓글 작성")
    @Parameters({
            @Parameter(name = "contents", description = "내용(1~500자)", example = "내용입니다."),
            @Parameter(name = "userId", description = "사용자 ID", example = "user1234")
    })
    public ResponseEntity<CommentResponseDto> createComment(@RequestParam Long scedule, @Valid @RequestBody CommentRequestDto requestDto) {
        // scedule id는 쿼리 파람으로 받아온다.
        return ResponseEntity.ok(commentService.createComment(scedule, requestDto));
    }

    @PutMapping("/comment")
    @Operation(summary = "댓글 수정")
    @Parameters({
            @Parameter(name = "contents", description = "내용(1~500자)", example = "내용입니다.")
    })
    public ResponseEntity<CommentResponseDto> updateComment(@RequestParam Long scedule, @RequestParam Long id, @Valid @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.updateComment(scedule, id, requestDto));
    }

    @DeleteMapping("/comment")
    @Operation(summary = "댓글 삭제")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID", example = "user1234")
    })
    public ResponseEntity<String> deleteSchedule(@RequestParam Long scedule, @RequestParam Long id, @RequestBody Map<String, String> userId) {
        return ResponseEntity.ok(commentService.deleteComment(scedule, id, userId.get("userId")));
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
