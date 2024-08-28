package com.example.kakao_mlms.service;

import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.ERole;
import com.example.kakao_mlms.dto.request.AuthSignUpDto;
import com.example.kakao_mlms.dto.request.UserResisterDto;
import com.example.kakao_mlms.dto.request.UserSignUpDto;
import com.example.kakao_mlms.dto.response.JwtTokenDto;
import com.example.kakao_mlms.exception.CommonException;
import com.example.kakao_mlms.exception.ErrorCode;
import com.example.kakao_mlms.repository.QnaRepository;
import com.example.kakao_mlms.repository.UserRepository;
import com.example.kakao_mlms.util.CookieUtil;
import com.example.kakao_mlms.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final QnaRepository qnaRepository;
    private final PasswordEncoder passwordEncoder;

    public Boolean resisterUser(UserSignUpDto requestDto) {
        userRepository.findBySerialIdOrNickname(requestDto.serialId(), requestDto.nickname())
                .ifPresent(u -> {
                    throw new CommonException(ErrorCode.DUPLICATION_IDORNICKNAME);
                });

        userRepository.save(User.signUp(new AuthSignUpDto(requestDto.serialId(),
                        requestDto.nickname()),
                passwordEncoder.encode(requestDto.password())));

        return Boolean.TRUE;
    }

    @Transactional
    public JwtTokenDto reissue(final String refreshToken) {
        return jwtUtil.reissue(refreshToken);
    }

    public Boolean withdrawUser(Long id, Long userId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        if (!(id == userId || user.getRole() == ERole.ADMIN))
            throw new CommonException(ErrorCode.ACCESS_DENIED_ERROR);
        //Qna는 삭제하지 않고 기본 유저 id로 전환
        qnaRepository.updateQnaByUserId(userId);

        userRepository.delete(user);
        return Boolean.TRUE;
    }

    public void registerUserInfo(Long userId, UserResisterDto requestDto, HttpServletResponse response) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        if(userRepository.existsByNickname(requestDto.nickname()))
            throw new CommonException(ErrorCode.DUPLICATION_IDORNICKNAME);

        user.register(requestDto.nickname());
        JwtTokenDto tokenDto = jwtUtil.generateTokens(userId, ERole.USER);

        CookieUtil.addSecureCookie(response, "refreshToken", tokenDto.getRefreshToken(), jwtUtil.getWebRefreshTokenExpirationSecond());
        CookieUtil.addCookie(response, "accessToken", tokenDto.getAccessToken());
        CookieUtil.addCookie(response, "nickname", URLEncoder.encode(requestDto.nickname(), StandardCharsets.UTF_8));
        CookieUtil.addCookie(response, "role", user.getRole().getDisplayName());
    }

    public Boolean updateUserInfo(Long userId, UserResisterDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        if(userRepository.existsByNickname(requestDto.nickname()))
            throw new CommonException(ErrorCode.DUPLICATION_IDORNICKNAME);

        user.updateInfo(requestDto.nickname());

        return Boolean.TRUE;
    }

    public String getRefreshToken(HttpServletRequest request) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        return refreshToken;
    }

    public void renewSecretCookie(HttpServletResponse response, JwtTokenDto tokenDto) {
        CookieUtil.addSecureCookie(response, "refreshToken", tokenDto.getRefreshToken(), jwtUtil.getWebRefreshTokenExpirationSecond());
    }
}
