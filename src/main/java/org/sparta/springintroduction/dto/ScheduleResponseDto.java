package org.sparta.springintroduction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sparta.springintroduction.entity.Schedule;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String charge;
    private LocalDateTime createdAt;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.charge = schedule.getCharge();
        this.createdAt = schedule.getCreatedAt();
    }
}
