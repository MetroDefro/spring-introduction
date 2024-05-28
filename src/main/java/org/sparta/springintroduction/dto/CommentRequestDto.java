package org.sparta.springintroduction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.springintroduction.entity.Comment;
import org.sparta.springintroduction.entity.Schedule;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank
    @Size(min = 1, max = 500)
    private String contents;

    public Comment toEntity(Schedule schedule, String username) {
        return Comment.builder()
                .contents(contents)
                .username(username)
                .schedule(schedule)
                .build();
    }
}
