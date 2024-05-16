package org.sparta.springintroduction.service;

import org.sparta.springintroduction.dto.ScheduleRequestDto;
import org.sparta.springintroduction.dto.ScheduleResponseDto;
import org.sparta.springintroduction.entity.Schedule;
import org.sparta.springintroduction.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        Schedule schedule = new Schedule(requestDto);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(savedSchedule);
    }

    public ScheduleResponseDto getSchedule(Long id) {
        return new ScheduleResponseDto(findScheduleById(id));
    }

    public List<ScheduleResponseDto> getSchedules() {
        return scheduleRepository.findAll().stream().map(ScheduleResponseDto::new).toList();
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = findScheduleById(id);
        if(checkPassword(schedule, requestDto.getPassword())) {
            return new ScheduleResponseDto(schedule.update(requestDto));
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public Long deleteSchedule(Long id, String password) {
        Schedule schedule = findScheduleById(id);
        if(checkPassword(schedule, password)) {
            scheduleRepository.delete(schedule);
            return id;
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));
    }

    private boolean checkPassword(Schedule schedule, String password) {
        return schedule.getPassword().equals(password);
    }
}
