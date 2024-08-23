package com.example.kakao_mlms.security.handler.signout;

import com.example.kakao_mlms.constant.Constants;
import com.example.kakao_mlms.util.HeaderUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSignOutResultHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        //헤더가 null이면 fail
        if (HeaderUtil.refineHeader(request, Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX).isEmpty())
            setFailureResponse(response);
        else
            setSuccessResponse(response);
    }

    private void setSuccessResponse(HttpServletResponse response) throws IOException {
        log.info("CustomSignOutResultHandler.setSuccessResponse");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("message", "Logout 완료");

        // Write response data as JSON.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JSONValue.toJSONString(result));
    }

    private void setFailureResponse(HttpServletResponse response) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", false);
        result.put("message", "Logout 실패");

        // Write response data as JSON.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(JSONValue.toJSONString(result));
    }
}
