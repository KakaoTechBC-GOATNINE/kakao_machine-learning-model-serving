package com.example.kakao_mlms.security;

import com.example.kakao_mlms.security.info.JwtUserInfo;
import com.example.kakao_mlms.security.jwt.BasicToken;
import com.example.kakao_mlms.security.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BasicAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailService customUserDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 전달 받은 UsernamePasswordAuthenticationToken

        JwtUserInfo jwtUserInfo = (JwtUserInfo) authentication.getPrincipal();

        CustomUserDetails userPrincipal = (CustomUserDetails) customUserDetailService.loadUserById(jwtUserInfo.id());

        if (userPrincipal.getRole() != jwtUserInfo.role()) {
            throw new AuthenticationException("Invalid Role") {};
        }

        // 인증 성공 시 UsernamePasswordAuthenticationToken 반환
        return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BasicToken.class);
    }
}
