package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.annotation.UserId;
import com.example.kakao_mlms.constant.Constants;
import com.example.kakao_mlms.dto.request.UserSignUpDto;
import com.example.kakao_mlms.dto.response.JwtTokenDto;
import com.example.kakao_mlms.exception.CommonException;
import com.example.kakao_mlms.exception.ErrorCode;
import com.example.kakao_mlms.exception.ResponseDto;
import com.example.kakao_mlms.service.AuthService;
import com.example.kakao_mlms.util.HeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //회원가입
    @PostMapping("/basic")
    public ResponseDto<?> resisterUser(@RequestBody @Valid UserSignUpDto requestDto) {
        return ResponseDto.ok(authService.resisterUser(requestDto));
    }

    //accessToken 다시 받기
    @PostMapping("/reissue")
    public ResponseDto<JwtTokenDto> reissue(final HttpServletRequest request) {
        final String refreshToken = HeaderUtil.refineHeader(request, Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX)
                .orElseThrow(() -> new CommonException(ErrorCode.INVALID_HEADER));

        final JwtTokenDto jwtTokenDto = authService.reissue(refreshToken);
        return ResponseDto.created(jwtTokenDto);
    }

    //회원탈퇴
    @DeleteMapping("/{userId}")
    public ResponseDto<?> withdrawUser(@UserId Long id, @PathVariable("userId") Long userId) {
        return ResponseDto.ok(authService.withdrawUser(id, userId));
    }
}