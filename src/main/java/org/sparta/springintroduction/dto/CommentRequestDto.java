package org.sparta.springintroduction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.springintroduction.entity.Comment;
import org.sparta.springintroduction.entity.Schedule;
import org.sparta.springintroduction.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    @Size(min = 1, max = 500, message = "내용은 최소 1자 이상, 500자 이하여야 합니다.")
    private String contents;

    public Comment toEntity(Schedule schedule, User user) {
        return Comment.builder()
                .contents(contents)
                .user(user)
                .schedule(schedule)
                .build();
    }
}
