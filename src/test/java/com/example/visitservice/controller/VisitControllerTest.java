package com.example.visitservice.controller;

import com.example.visitservice.exception.InvalidRequestStateException;
import com.example.visitservice.model.Visit;
import com.example.visitservice.request.FulfillmentRequest;
import com.example.visitservice.response.TransactionRecord;
import com.example.visitservice.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VisitController.class)
class VisitControllerTest {
    @MockBean
    private VisitService visitService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addVisitForRequest_rejectsEmptyRequest() throws Exception {
        mockMvc.perform(post("/visit/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addVisitForRequest_returnsTransactionRecord() throws Exception {
        final FulfillmentRequest fulfillmentRequest = new FulfillmentRequest(1, 2, 3, 4.0, "everything!");
        final TransactionRecord expectedRecord = new TransactionRecord(1.0, 2.0, 3.0, "aaaa-bbbb-cccc-dddd");

        Mockito.when(visitService.handleVisitFulfillment(ArgumentMatchers.any())).thenReturn(expectedRecord);

        mockMvc.perform(post("/visit/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fulfillmentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedRecord)));
    }

    @Test
    void addVisitForRequest_handlesInvalidException() throws Exception {
        final FulfillmentRequest fulfillmentRequest = new FulfillmentRequest(1, 2, 3, 4.0, "everything!");
        Mockito.when(visitService.handleVisitFulfillment(any())).thenThrow(new InvalidRequestStateException(1));

        mockMvc.perform(post("/visit/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fulfillmentRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"errorMessage\":\"1 is not a valid request; it either doesn't exist or has already been fulfilled.\"}"));
    }

    @Test
    void getVisitById_400BadFormat() throws Exception {
        mockMvc.perform(get("/visit/dfd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Visit request id was not in a valid format!\"}"));
    }

    @Test
    void getVisitById_200GoodFormat() throws Exception {
        final Visit expectedVisit = new Visit(1, 2, 3, 12934, 1.0, "everything!", "aaaa-bbbb-cccc-dddd");
        Mockito.when(visitService.getVisitById(anyInt())).thenReturn(expectedVisit);

        mockMvc.perform(get("/visit/3"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedVisit)));
    }
}