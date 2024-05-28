package org.sparta.springintroduction.service;

import lombok.RequiredArgsConstructor;
import org.sparta.springintroduction.dto.ScheduleRequestDto;
import org.sparta.springintroduction.dto.ScheduleResponseDto;
import org.sparta.springintroduction.entity.File;
import org.sparta.springintroduction.entity.Schedule;
import org.sparta.springintroduction.entity.User;
import org.sparta.springintroduction.repository.ScheduleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final FileService fileService;

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, MultipartFile multiFile, User user) {
        File file = null;
        if(multiFile != null && !multiFile.isEmpty()) {
            file = fileService.createFile(multiFile);
        }
        Schedule schedule = requestDto.toEntity(user.getUsername(), file);
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
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto, MultipartFile file, User user) {
        Schedule schedule = findScheduleById(id);
        if(checkUsername(schedule, user.getUsername())) {
            if(file != null && !file.isEmpty()) {
                fileService.updateFile(schedule.getFile().getId(), file);
            }
            return new ScheduleResponseDto(schedule
                    .update(requestDto.getTitle(), requestDto.getContents(), user.getUsername()));
        } else {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    public Long deleteSchedule(Long id, User user) {
        Schedule schedule = findScheduleById(id);
        if(checkUsername(schedule, user.getUsername())) {
            fileService.deleteFile(schedule.getFile().getId());
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
