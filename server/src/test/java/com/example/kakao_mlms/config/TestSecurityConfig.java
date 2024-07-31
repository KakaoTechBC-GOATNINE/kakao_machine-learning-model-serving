package com.example.kakao_mlms.config;

import com.example.kakao_mlms.security.CustomAuthenticationProvider;
import com.example.kakao_mlms.security.JwtAuthEntryPoint;
import com.example.kakao_mlms.security.config.SecurityConfig;
import com.example.kakao_mlms.security.handler.JwtAccessDeniedHandler;
import com.example.kakao_mlms.security.handler.signin.OAuth2LoginFailureHandler;
import com.example.kakao_mlms.security.handler.signin.OAuth2LoginSuccessHandler;
import com.example.kakao_mlms.security.handler.signout.CustomSignOutProcessHandler;
import com.example.kakao_mlms.security.handler.signout.CustomSignOutResultHandler;
import com.example.kakao_mlms.security.service.CustomOAuth2UserService;
import com.example.kakao_mlms.security.service.CustomUserDetailService;
import com.example.kakao_mlms.util.JwtUtil;
import jakarta.servlet.ServletException;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockBean private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @MockBean private JwtUtil jwtUtil;
    @MockBean private CustomSignOutProcessHandler customSignOutProcessHandler;
    @MockBean private CustomSignOutResultHandler customSignOutResultHandler;
    @MockBean private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    @MockBean private OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    @MockBean private CustomOAuth2UserService customOAuth2UserService;
    @MockBean private CustomAuthenticationProvider customAuthenticationProvider;


    @BeforeTestMethod
    public void securitySetUp() {
    }

}
