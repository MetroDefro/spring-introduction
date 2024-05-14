package org.sparta.springintroduction.dto;

import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    private String title;
    private String contents;
    private String charge;
    private String password;
}
