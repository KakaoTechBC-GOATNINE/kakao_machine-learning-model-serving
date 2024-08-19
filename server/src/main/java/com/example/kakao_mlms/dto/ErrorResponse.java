package com.example.kakao_mlms.dto;

import com.example.kakao_mlms.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ErrorResponse {
    protected final void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("data", null);
        map.put("error", new ExceptionDto(errorCode));

        response.getWriter().print(JSONValue.toJSONString(map));
    }
}
