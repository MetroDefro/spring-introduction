package org.sparta.springintroduction.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sparta.springintroduction.entity.Schedule;

@Getter
@AllArgsConstructor
public class ScheduleRequestDto {

    @NotNull
    @Size(min = 1, max = 200)
    private String title;

    @NotNull
    @Size(min = 1, max = 500)
    private String contents;

    @NotNull
    @Email
    private String charge;;

    public Schedule toEntity(String username) {
        return Schedule.builder()
                .title(title)
                .contents(contents)
                .charge(charge)
                .username(username)
                .build();
    }
}
