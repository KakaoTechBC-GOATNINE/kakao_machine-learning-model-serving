package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.annotation.UserId;
import com.example.kakao_mlms.constant.Constants;
import com.example.kakao_mlms.dto.request.UserResisterDto;
import com.example.kakao_mlms.dto.request.UserSignUpDto;
import com.example.kakao_mlms.dto.response.JwtTokenDto;
import com.example.kakao_mlms.exception.CommonException;
import com.example.kakao_mlms.exception.ErrorCode;
import com.example.kakao_mlms.exception.ResponseDto;
import com.example.kakao_mlms.service.AuthService;
import com.example.kakao_mlms.util.HeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    //소셜 로그인 사용자 정보 등록
    @PatchMapping("/resister")
    public ResponseEntity<Void> resister(@UserId Long id,
                                   @RequestBody UserResisterDto requestDto,
                                   HttpServletResponse response) {
        authService.registerUserInfo(id, requestDto, response);
        return ResponseEntity.ok().build();
    }

    //사용자 정보 수정
    @PostMapping("/update")
    public ResponseDto<?> updateUserInfo(@UserId Long id, @RequestBody UserResisterDto requestDto) {
        return ResponseDto.created(authService.updateUserInfo(id, requestDto));
    }

    //accessToken 다시 받기
    @PostMapping("/reissue")
    public ResponseDto<JwtTokenDto> reissue(final HttpServletRequest request, HttpServletResponse response) {
        final String refreshToken = authService.getRefreshToken(request);
        log.info("refreshToken : {}", refreshToken);
        if (refreshToken == null) return null;
        final JwtTokenDto jwtTokenDto = authService.reissue(refreshToken);
        authService.renewSecretCookie(response, jwtTokenDto);
        return ResponseDto.created(jwtTokenDto);
    }

    //회원탈퇴
    @DeleteMapping("/{userId}")
    public ResponseDto<?> withdrawUser(@UserId Long id, @PathVariable("userId") Long userId) {
        return ResponseDto.ok(authService.withdrawUser(id, userId));
    }
}
