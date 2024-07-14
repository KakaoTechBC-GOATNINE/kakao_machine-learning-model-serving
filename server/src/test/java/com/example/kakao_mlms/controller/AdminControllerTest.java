package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private UserService userService;
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
}