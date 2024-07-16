package com.example.kakao_mlms.security.handler.signout;

import com.example.kakao_mlms.constant.Constants;
import com.example.kakao_mlms.exception.CommonException;
import com.example.kakao_mlms.exception.ErrorCode;
import com.example.kakao_mlms.repository.UserRepository;
import com.example.kakao_mlms.security.CustomUserDetails;
import com.example.kakao_mlms.security.service.KakaoLogoutService;
import com.example.kakao_mlms.util.HeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSignOutProcessHandler implements LogoutHandler {
    private final UserRepository userRepository;
    private final KakaoLogoutService kakaoLogoutService;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        if (authentication == null) {
            return;
        }

        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

        String token = HeaderUtil.refineHeader(request, Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX)
                .orElseThrow(() -> new CommonException(ErrorCode.INVALID_HEADER));

        //kakaoLogoutService.logoutFromKakao(token);

        userRepository.updateRefreshTokenAndLoginStatus(userPrincipal.getId(), null, false);
    }
}
