package org.sparta.springintroduction.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sparta.springintroduction.entity.Comment;
import org.sparta.springintroduction.entity.Schedule;

@Getter
@AllArgsConstructor
public class CommentRequestDto {

    @NotNull
    @Size(min = 1, max = 500)
    private String contents;

    @NotNull
    private String userId;

    public Comment toEntity(Schedule schedule) {
        return Comment.builder()
                .contents(contents)
                .userId(userId)
                .schedule(schedule)
                .build();
    }
}
