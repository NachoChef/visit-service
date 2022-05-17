package com.example.visitservice.controller;

import com.example.visitservice.model.VisitRequest;
import com.example.visitservice.repository.VisitRequestRepository;
import com.example.visitservice.request.NewVisitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VisitRequestController.class)
class VisitRequestControllerTest {
    @MockBean
    private VisitRequestRepository mockRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createNewVisitRequest_400EmptyBody() throws Exception {
        mockMvc.perform(post("/visit-request/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewVisitRequest_400BadFormat() throws Exception {
        mockMvc.perform(post("/visit-request/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"requestorId\":\"abc\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewVisitRequest_ReturnsRequestDetails() throws Exception {
        final NewVisitRequest nve = new NewVisitRequest(1);
        final VisitRequest expected = VisitRequest.fromRequest(nve);

        Mockito.when(mockRepository.save(ArgumentMatchers.any())).thenReturn(expected);

        mockMvc.perform(post("/visit-request/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nve)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void cancelVisitRequest_400BadFormat() throws Exception {
        mockMvc.perform(patch("/visit-request/cancel/dfd"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelVisitRequest_200GoodFormat() throws Exception {
        Mockito.doNothing().when(mockRepository).cancelRequestById(ArgumentMatchers.eq(3));

        mockMvc.perform(patch("/visit-request/cancel/3"))
                .andExpect(status().isOk());
    }
}