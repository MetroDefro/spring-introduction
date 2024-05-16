package org.sparta.springintroduction.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparta.springintroduction.dto.ScheduleRequestDto;
import org.sparta.springintroduction.dto.ScheduleResponseDto;
import org.sparta.springintroduction.entity.Schedule;
import org.sparta.springintroduction.repository.ScheduleRepository;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository repository;

    @InjectMocks
    private ScheduleService service;


    @DisplayName("일정 작성 성공")
    @Test
    void createSchedule() {
        LocalDateTime now = LocalDateTime.now();

        // given
        Schedule schedule = new Schedule(1L, "제목", "내용", "damdang@email.com", "1234", now);
        ScheduleRequestDto requestDto =
                new ScheduleRequestDto("제목", "내용", "damdang@email.com", "1234");
        ScheduleResponseDto responseDto =
                new ScheduleResponseDto(1L, "제목", "내용", "damdang@email.com", now);

        given(repository.save(any(Schedule.class)))
                .willReturn(schedule);

        // when
        ScheduleResponseDto testResponseDto = service.createSchedule(requestDto);

        // then
        Assertions.assertEquals(responseDto.getId(), testResponseDto.getId());
        Assertions.assertEquals(responseDto.getTitle(), testResponseDto.getTitle());
        Assertions.assertEquals(responseDto.getContents(), testResponseDto.getContents());
        Assertions.assertEquals(responseDto.getCharge(), testResponseDto.getCharge());
        Assertions.assertEquals(responseDto.getCreatedAt(), testResponseDto.getCreatedAt());
    }

    @DisplayName("선택한 일정 조회 성공")
    @Test
    void getSchedule() {
        LocalDateTime now = LocalDateTime.now();

        // given
        Schedule schedule = new Schedule(1L, "제목", "내용", "damdang@email.com", "1234", now);
        ScheduleResponseDto responseDto =
                new ScheduleResponseDto(1L, "제목", "내용", "damdang@email.com", now);

        given(repository.findById(any(Long.class)))
                .willReturn(Optional.of(schedule));

        // when
        ScheduleResponseDto testResponseDto = service.getSchedule(1L);

        // then
        Assertions.assertEquals(responseDto.getId(), testResponseDto.getId());
        Assertions.assertEquals(responseDto.getTitle(), testResponseDto.getTitle());
        Assertions.assertEquals(responseDto.getContents(), testResponseDto.getContents());
        Assertions.assertEquals(responseDto.getCharge(), testResponseDto.getCharge());
        Assertions.assertEquals(responseDto.getCreatedAt(), testResponseDto.getCreatedAt());
    }

    @DisplayName("일정 목록 조회 성공")
    @Test
    void getSchedules() {
        LocalDateTime now = LocalDateTime.now();

        // given
        List<ScheduleResponseDto> responseDtos = List.of(
                new ScheduleResponseDto(1L, "제목1", "내용1", "damdang1@email.com", now),
                new ScheduleResponseDto(2L, "제목2", "내용2", "damdang2@email.com", now));
        List<Schedule> schedules = List.of(
                new Schedule(1L, "제목1", "내용1", "damdang1@email.com", "1234", now),
                new Schedule(2L, "제목2", "내용2", "damdang2@email.com", "1234", now));

        given(repository.findAll()).willReturn(schedules);

        // when
        List<ScheduleResponseDto> testResponseDtos = service.getSchedules();

        // then
        for(int i = 0; i < schedules.size(); i++) {
            Assertions.assertEquals(responseDtos.get(i).getId(), testResponseDtos.get(i).getId());
            Assertions.assertEquals(responseDtos.get(i).getTitle(), testResponseDtos.get(i).getTitle());
            Assertions.assertEquals(responseDtos.get(i).getContents(), testResponseDtos.get(i).getContents());
            Assertions.assertEquals(responseDtos.get(i).getCharge(), testResponseDtos.get(i).getCharge());
            Assertions.assertEquals(responseDtos.get(i).getCreatedAt(), testResponseDtos.get(i).getCreatedAt());
        }
    }

    @DisplayName("선택한 일정 수정 성공")
    @Test
    void updateSchedule() {
        LocalDateTime now = LocalDateTime.now();

        // given
        Schedule schedule = new Schedule(1L, "제목1", "내용1", "damdang1@email.com", "1234", now);
        ScheduleRequestDto requestDto =
                new ScheduleRequestDto("제목2", "내용2", "damdang2@email.com", "1234");
        ScheduleResponseDto responseDto =
                new ScheduleResponseDto(1L, "제목2", "내용2", "damdang2@email.com", now);
        given(repository.findById(any(Long.class)))
                .willReturn(Optional.of(schedule));

        // when
        ScheduleResponseDto testResponseDto = service.updateSchedule(1L, requestDto);

        // then
        Assertions.assertEquals(responseDto.getId(), testResponseDto.getId());
        Assertions.assertEquals(responseDto.getTitle(), testResponseDto.getTitle());
        Assertions.assertEquals(responseDto.getContents(), testResponseDto.getContents());
        Assertions.assertEquals(responseDto.getCharge(), testResponseDto.getCharge());
        Assertions.assertEquals(responseDto.getCreatedAt(), testResponseDto.getCreatedAt());
    }

    @DisplayName("선택한 일정 삭제 성공")
    @Test
    void deleteSchedule() {
        LocalDateTime now = LocalDateTime.now();

        // given
        Schedule schedule = new Schedule(1L, "제목", "내용", "damdang@email.com", "1234", now);
        given(repository.findById(any(Long.class)))
                .willReturn(Optional.of(schedule));

        // when
        Long testId = service.deleteSchedule(schedule.getId(), "1234");

        // then
        Assertions.assertEquals(schedule.getId(), testId);
    }

    @DisplayName("일정 조회 실패")
    @Test
    void getScheduleFail() {
        // given
        given(repository.findById(any(Long.class)))
                .willThrow(IllegalArgumentException.class);

        // when

        // then
        assertThrows(IllegalArgumentException.class, () ->  service.getSchedule(2L));
    }


    @DisplayName("일정 수정 실패(비밀번호 틀림)")
    @Test
    void updateScheduleFail() {
        LocalDateTime now = LocalDateTime.now();

        // given
        Schedule schedule = new Schedule(1L, "제목1", "내용1", "damdang1@email.com", "12345", now);
        ScheduleRequestDto requestDto =
                new ScheduleRequestDto("제목2", "내용2", "damdang2@email.com", "1234");

        given(repository.findById(any(Long.class)))
                .willReturn(Optional.of(schedule));

        // when

        // then
        assertThrows(IllegalArgumentException.class, () ->  service.updateSchedule(schedule.getId(), requestDto));
    }
    
    @DisplayName("일정 삭제 실패(비밀번호 틀림)")
    @Test
    void deleteScheduleFail() {
        LocalDateTime now = LocalDateTime.now();

        // given
        Schedule schedule = new Schedule(1L, "제목", "내용", "damdang@email.com", "1234", now);
        given(repository.findById(any(Long.class)))
                .willReturn(Optional.of(schedule));

        // when

        // then
        assertThrows(IllegalArgumentException.class, () ->  service.deleteSchedule(schedule.getId(), "12345"));
    }
}