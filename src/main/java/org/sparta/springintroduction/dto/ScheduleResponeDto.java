package org.sparta.springintroduction.dto;

import lombok.Getter;
import org.sparta.springintroduction.entity.Schedule;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponeDto {
    private Long id;
    private String title;
    private String contents;
    private String charge;
    private String password;
    private LocalDateTime createdAt;

    public ScheduleResponeDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.charge = schedule.getCharge();
        this.password = schedule.getPassword();
        this.createdAt = schedule.getCreatedAt();
    }
}
