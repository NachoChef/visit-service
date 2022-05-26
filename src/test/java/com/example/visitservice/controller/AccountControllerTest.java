package com.example.visitservice.controller;

import com.example.visitservice.model.User;
import com.example.visitservice.repository.UserRepository;
import com.example.visitservice.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @MockBean
    private UserRepository mockRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createMember_emptyRequest400s() throws Exception {
        mockMvc.perform(post("/account/member/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void createMember_missingField400s() throws Exception {
        mockMvc.perform(post("/account/member/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Billy\", \"lastName\":\"Testerson\"}"))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void createMember_validFieldsReturnNewUser() throws Exception {
        final User expectedUser = User.fromUserRequestAndRole(userRequest(), User.UserRole.MEMBER);
        Mockito.when(mockRepository.save(ArgumentMatchers.any())).thenReturn(expectedUser);

        mockMvc.perform(post("/account/member/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUser)));
    }

    @Test
    void createMember_fieldValidationFailsBadEmail() throws Exception {
        final UserRequest request = UserRequest.builder().firstName("Billy").lastName("Testerson").email("something").build();
        mockMvc.perform(post("/account/member/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPal_emptyRequest400s() throws Exception {
        mockMvc.perform(post("/account/pal/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void createPal_missingField400s() throws Exception {
        mockMvc.perform(post("/account/pal/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Billy\", \"lastName\":\"Testerson\"}"))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void createPal_validFieldsReturnNewUser() throws Exception {
        final User expectedUser = User.fromUserRequestAndRole(userRequest(), User.UserRole.PAL);
        Mockito.when(mockRepository.save(ArgumentMatchers.any())).thenReturn(expectedUser);

        mockMvc.perform(post("/account/member/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUser)));
    }

    @Test
    void createPal_fieldValidationFailsBadEmail() throws Exception {
        final UserRequest request = UserRequest.builder().firstName("Billy").lastName("Testerson").email("something").build();
        mockMvc.perform(post("/account/pal/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserInfo_returnsInfoWhenExists() throws Exception {
        final User expectedUser = User.fromUserRequestAndRole(userRequest(), User.UserRole.MEMBER);
        Mockito.when(mockRepository.findById(ArgumentMatchers.eq(1))).thenReturn(Optional.of(expectedUser));

        mockMvc.perform(get("/account/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUser)));
    }

    @Test
    void getUserInfo_returnsNullWhenDoesntExist() throws Exception {
        Mockito.when(mockRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(get("/account/6"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserInfo_400WhenIdIsNotInt() throws Exception {
        mockMvc.perform(get("/account/abc"))
                .andExpect(status().isBadRequest());
    }

    private UserRequest userRequest() {
        return UserRequest.builder().firstName("Billy").lastName("Testerson").email("billyt@testing.com").build();
    }
}