package org.sparta.springintroduction.controller;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sparta.springintroduction.dto.ScheduleRequestDto;
import org.sparta.springintroduction.dto.ScheduleResponseDto;
import org.sparta.springintroduction.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService service;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("일정 작성 성공")
    @Test
    void createSchedule() throws Exception {
        objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);

        // given
        ScheduleRequestDto requestDto =
                new ScheduleRequestDto("제목", "내용", "damdang@email.com", "1234");
        ScheduleResponseDto responseDto =
                new ScheduleResponseDto(1L, "제목", "내용", "damdang@email.com", LocalDateTime.now());
        String content = objectMapper.writeValueAsString(requestDto);
        given(service.createSchedule(any(ScheduleRequestDto.class))).willReturn(responseDto);

        // when
        ResultActions actions = mockMvc.perform(post("/sprig-introduction/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        MvcResult result = actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        System.out.println("result: " + result.getResponse().getContentAsString());
    }

    @DisplayName("선택한 일정 조회 성공")
    @Test
    void getSchedule() throws Exception {
        objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);

        // given
        ScheduleResponseDto responseDto =
                new ScheduleResponseDto(1L, "제목", "내용", "damdang@email.com", LocalDateTime.now());
        given(service.getSchedule(any(Long.class))).willReturn(responseDto);

        // when
        ResultActions actions = mockMvc.perform(get("/sprig-introduction/schedule")
                .param("id", String.valueOf(responseDto.getId()))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult result = actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        System.out.println("result: " + result.getResponse().getContentAsString());
    }

    @DisplayName("일정 목록 조회 성공")
    @Test
    void getSchedules() throws Exception {
        objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);

        // given
        List<ScheduleResponseDto> responseDtos = List.of(
                new ScheduleResponseDto(1L, "제목1", "내용1", "damdang1@email.com", LocalDateTime.now()),
                new ScheduleResponseDto(2L, "제목2", "내용2", "damdang2@email.com", LocalDateTime.now()));

        given(service.getSchedules()).willReturn(responseDtos);

        // when, then
        ResultActions actions = mockMvc.perform(get("/sprig-introduction/schedules")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult result = actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        System.out.println("result: " + result.getResponse().getContentAsString());
    }

    @DisplayName("선택한 일정 수정 성공")
    @Test
    void updateSchedule() throws Exception {
        objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);

        // given
        ScheduleRequestDto requestDto =
                new ScheduleRequestDto("제목1", "내용1", "damdang1@email.com", "1234");
        ScheduleResponseDto responseDto =
                new ScheduleResponseDto(1L, "제목2", "내용2", "damdang2@email.com", LocalDateTime.now());
        String content = objectMapper.writeValueAsString(requestDto);
        given(service.updateSchedule(any(Long.class), any(ScheduleRequestDto.class))).willReturn(responseDto);

        // when
        ResultActions actions = mockMvc.perform(put("/sprig-introduction/schedule")
                .param("id", String.valueOf(responseDto.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(content));

        // then
        MvcResult result = actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        System.out.println("result: " + result.getResponse().getContentAsString());
    }

    @DisplayName("선택한 일정 삭제 성공")
    @Test
    void deleteSchedule() throws Exception {
        objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);

        // given
        given(service.deleteSchedule(any(Long.class), any(String.class))).willReturn(1L);

        // when
        ResultActions actions = mockMvc.perform(delete("/sprig-introduction/schedule")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"1234\"}"));

        // then
        MvcResult result = actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        System.out.println("result: " + result.getResponse().getContentAsString());
    }

    @DisplayName("일정 작성 실패")
    @Test
    void createScheduleFail() throws Exception {
        objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);

        // given
        ScheduleRequestDto requestDto =
                new ScheduleRequestDto("제목", "내용", "damdang@email.com", "");
        ScheduleResponseDto responseDto =
                new ScheduleResponseDto(1L, "제목", "내용", "damdang@email.com", LocalDateTime.now());
        String content = objectMapper.writeValueAsString(requestDto);
        given(service.createSchedule(any(ScheduleRequestDto.class))).willReturn(responseDto);

        // when
        ResultActions actions = mockMvc.perform(post("/sprig-introduction/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        MvcResult result = actions.andExpect(status().is4xxClientError())
                .andDo(print())
                .andReturn();
        System.out.println("result: " + result.getResponse().getContentAsString());
    }

    @DisplayName("선택한 일정 삭제 실패")
    @Test
    void deleteScheduleFail() throws Exception {
        objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);

        // given
        given(service.deleteSchedule(any(Long.class), any(String.class)))
                .willThrow(new IllegalArgumentException("비밀번호가 일치하지 않습니다."));

        // when
        ResultActions actions = mockMvc.perform(delete("/sprig-introduction/schedule")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"12345\"}"));

        // then
        MvcResult result = actions.andExpect(status().is4xxClientError())
                .andDo(print())
                .andReturn();

        System.out.println("result: " + result.getResponse().getContentAsString());
    }
}