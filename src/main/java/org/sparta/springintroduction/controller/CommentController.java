package org.sparta.springintroduction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sparta.springintroduction.dto.CommentRequestDto;
import org.sparta.springintroduction.dto.CommentResponseDto;
import org.sparta.springintroduction.security.UserDetailsImpl;
import org.sparta.springintroduction.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

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
    })
    public ResponseEntity<CommentResponseDto> createComment(@RequestParam Long schedule,
                                                            @Valid @RequestBody CommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // schedule id는 쿼리 파람으로 받아온다.
        return ResponseEntity.ok(commentService.createComment(schedule, requestDto, userDetails.getUser()));
    }

    @PutMapping("/comment")
    @Operation(summary = "댓글 수정")
    @Parameters({
            @Parameter(name = "contents", description = "내용(1~500자)", example = "내용입니다.")
    })
    public ResponseEntity<CommentResponseDto> updateComment(@RequestParam Long schedule, @RequestParam Long id,
                                                            @Valid @RequestBody CommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentService.updateComment(schedule, id, requestDto, userDetails.getUser()));
    }

    @DeleteMapping("/comment")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<String> deleteSchedule(@RequestParam Long schedule, @RequestParam Long id,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentService.deleteComment(schedule, id, userDetails.getUser()));
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
