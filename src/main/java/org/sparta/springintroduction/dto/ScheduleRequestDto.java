package org.sparta.springintroduction.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sparta.springintroduction.entity.File;
import org.sparta.springintroduction.entity.Schedule;
import org.sparta.springintroduction.entity.User;

@Getter
@AllArgsConstructor
public class ScheduleRequestDto {

    @NotBlank(message = "제목은 공백일 수 없습니다.")
    @Size(min = 1, max = 200, message = "제목은 최소 1자 이상, 200자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    @Size(min = 1, max = 500, message = "내용은 최소 1자 이상, 500자 이하여야 합니다.")
    private String contents;

    @NotBlank(message = "email은 공백일 수 없습니다.")
    @Email(message = "이메일 형식에 맞추어야 합니다.")
    private String charge;;

    public Schedule toEntity(User user, File file) {
        return Schedule.builder()
                .title(title)
                .contents(contents)
                .charge(charge)
                .user(user)
                .file(file)
                .build();
    }
}
