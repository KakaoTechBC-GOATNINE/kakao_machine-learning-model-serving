package com.example.kakao_mlms.security.handler.signin;

import com.example.kakao_mlms.dto.ErrorResponse;
import com.example.kakao_mlms.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DefaultSignInFailureHandler extends ErrorResponse implements AuthenticationFailureHandler{
    @Value("${after-login.default-fail}")
    private String LOGIN_URL;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        setFailureAppResponse(response);

    }

    private void setFailureAppResponse(HttpServletResponse response) throws IOException{
        //Header 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(ErrorCode.FAILURE_LOGIN.getHttpStatus().value());

        //return Json 생생
        setErrorResponse(response, ErrorCode.FAILURE_LOGIN);
    }
}
