package com.example.kakao_mlms.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@Slf4j
public class HeaderUtil {
    public static Optional<String> refineHeader(HttpServletRequest request, String header, String prefix) {
        String unpreparedToken = request.getHeader(header);

        if (!StringUtils.hasText(unpreparedToken) || !unpreparedToken.startsWith(prefix)) {
            log.info("토큰 X");
            return Optional.empty();
        }

        return Optional.of(unpreparedToken.substring(prefix.length()));
    }
}
