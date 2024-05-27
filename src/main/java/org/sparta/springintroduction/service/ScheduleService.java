package org.sparta.springintroduction.service;

import org.sparta.springintroduction.dto.ScheduleRequestDto;
import org.sparta.springintroduction.dto.ScheduleResponseDto;
import org.sparta.springintroduction.entity.Schedule;
import org.sparta.springintroduction.entity.User;
import org.sparta.springintroduction.repository.ScheduleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, User user) {
        Schedule schedule = requestDto.toEntity(user.getUsername());
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(savedSchedule);
    }

    public ScheduleResponseDto getSchedule(Long id) {
        return new ScheduleResponseDto(findScheduleById(id));
    }

    public List<ScheduleResponseDto> getSchedules() {
        return scheduleRepository.findAll(Sort.by("createdAt").descending())
                .stream().map(ScheduleResponseDto::new).toList();
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto, User user) {
        Schedule schedule = findScheduleById(id);
        if(checkUsername(schedule, user.getUsername())) {
            return new ScheduleResponseDto(schedule
                    .update(requestDto.getTitle(), requestDto.getContents(), user.getUsername()));
        } else {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    public Long deleteSchedule(Long id, User user) {
        Schedule schedule = findScheduleById(id);
        if(checkUsername(schedule, user.getUsername())) {
            scheduleRepository.delete(schedule);
            return id;
        } else {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));
    }

    private boolean checkUsername(Schedule schedule, String username) {
        return schedule.getUsername().equals(username);
    }
}
