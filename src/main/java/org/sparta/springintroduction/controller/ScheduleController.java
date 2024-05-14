package org.sparta.springintroduction.controller;

import org.sparta.springintroduction.service.ScheduleService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduleController {

    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
}
