package com.example.kakao_mlms.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
@Slf4j
public class UserIdInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("설정");
        log.info(authentication.getName() + " : " + authentication.getAuthorities().stream().findFirst()
                .map(GrantedAuthority::getAuthority).orElseThrow());
        request.setAttribute("USER_ID", authentication.getName());

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
