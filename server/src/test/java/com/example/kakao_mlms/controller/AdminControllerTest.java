package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.KakaoMlmsApplication;
import com.example.kakao_mlms.service.QnaService;
import com.example.kakao_mlms.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private UserService userService;
    @MockBean private QnaService qnaService;
    private final String BASE_URL = "/api/v1/admins";

    @Test
    void getAllUsers() throws Exception {
        // Given
        given(userService.searchUsers(eq(null), any(Pageable.class))).willReturn(Page.empty());

        // When & Then
        mvc.perform(get(BASE_URL + "/users"))
                .andExpect(status().isUnauthorized());
//        then(userService).should().searchUsers(eq(null), any(Pageable.class));
    }

    @Test
    void getAllQnas() throws Exception {
        // Given
        given(qnaService.searchQnas(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());

        // When & Then
        mvc.perform(get(BASE_URL + "/qnas"))
                .andExpect(status().isUnauthorized());
//        then(qnaService).should().searchQnas(eq(null), eq(null), any(Pageable.class));
    }


}