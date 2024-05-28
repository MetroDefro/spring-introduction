package org.sparta.springintroduction.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sparta.springintroduction.entity.File;
import org.sparta.springintroduction.entity.Schedule;

@Getter
@AllArgsConstructor
public class ScheduleRequestDto {

    @NotBlank
    @Size(min = 1, max = 200)
    private String title;

    @NotBlank
    @Size(min = 1, max = 500)
    private String contents;

    @NotBlank
    @Email
    private String charge;;

    public Schedule toEntity(String username, File file) {
        return Schedule.builder()
                .title(title)
                .contents(contents)
                .charge(charge)
                .username(username)
                .file(file)
                .build();
    }
}
