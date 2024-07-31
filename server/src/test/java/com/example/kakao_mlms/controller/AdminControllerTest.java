package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.config.TestSecurityConfig;
import com.example.kakao_mlms.domain.type.ERole;
import com.example.kakao_mlms.dto.QnaDtoWithImages;
import com.example.kakao_mlms.dto.response.UserDto;
import com.example.kakao_mlms.service.AnswerService;
import com.example.kakao_mlms.service.QnaService;
import com.example.kakao_mlms.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @MockBean private UserService userService;
    @MockBean private QnaService qnaService;
    @MockBean private AnswerService answerService;
    private final String BASE_URI = "/api/v1/admins";

    @WithMockUser(username = "tester", roles = "ADMIN")
    @Test
    void getAllUsers_ByAdmin() throws Exception {
        // Given
        given(userService.searchUsers(eq(null), any(Pageable.class)))
                .willReturn(createUsers("tester", ERole.ADMIN));

        // When & Then
        mvc.perform(get(BASE_URI + "/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        then(userService).should().searchUsers(eq(null), any(Pageable.class));
    }

    @WithMockUser(username = "tester", roles = "USER")
    @Test
    void getAllUsers_ByUser() throws Exception {
        // Given
        given(userService.searchUsers(eq(null), any(Pageable.class)))
                .willReturn(createUsers("tester", ERole.USER));

        // When & Then
        mvc.perform(get(BASE_URI + "/users"))
                .andExpect(status().isForbidden());
        then(userService).should().searchUsers(eq(null), any(Pageable.class));
    }

    @WithMockUser(username = "tester", roles = "ADMIN")
    @Test
    void getAllQnas() throws Exception {
        // Given
        given(qnaService.searchQnas(eq(null), eq(null), any(Pageable.class))).willReturn(createQnas());

        // When & Then
        mvc.perform(get(BASE_URI + "/qnas"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        then(qnaService).should().searchQnas(eq(null), eq(null), any(Pageable.class));
    }

//    @WithMockUser(username = "tester", roles = "ADMIN")
    @Test
    void replyQna() throws Exception {
        // Given
        Long qnaId = 1L;
        String content = "content";

        // When & Then
        mvc.perform(post(BASE_URI + "/qnas/" + qnaId)
                .content(mapper.writeValueAsString(content))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value(qnaId));
        then(answerService).should().replyQna(anyLong(), qnaId, content);
    }

    private Page<UserDto> createUsers(String serialId, ERole role) {
        return new PageImpl<>(List.of(UserDto.of(serialId, role)), PageRequest.of(0, 1), 1);
    }

    private Page<QnaDtoWithImages> createQnas() {
        return new PageImpl<>(List.of(QnaDtoWithImages.of()), PageRequest.of(0, 1), 1);
    }

}