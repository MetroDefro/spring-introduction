package org.sparta.springintroduction.controller;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sparta.springintroduction.dto.ScheduleResponseDto;
import org.sparta.springintroduction.service.FileService;
import org.sparta.springintroduction.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService service;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("파일 업로드 페이지 연결 성공")
    @Test
    void upload() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(get("/sprig-introduction/upload")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult result = actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        System.out.println("result: " + result.getResponse().getContentAsString());
    }

    @DisplayName("파일 업로드 성공")
    @Test
    void fileUpload() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(post("/sprig-introduction/upload")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult result = actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        System.out.println("result: " + result.getResponse().getContentAsString());
    }
}