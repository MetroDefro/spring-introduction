package org.sparta.springintroduction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sparta.springintroduction.entity.Comment;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String contents;
    private Long scheduleId;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.scheduleId = comment.getSchedule().getId();
        this.createdAt = comment.getCreatedAt();
    }
}
