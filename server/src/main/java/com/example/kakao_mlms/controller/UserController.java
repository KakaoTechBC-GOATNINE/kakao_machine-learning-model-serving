package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.annotation.UserId;
import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.dto.response.UserInfoDto;
import com.example.kakao_mlms.exception.ResponseDto;
import com.example.kakao_mlms.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //유저정보 불러오기
    @GetMapping("")
    public ResponseDto<UserInfoDto> getUserInfo(@UserId Long id) {
        final User user = userService.getUserInfo(id);
        final UserInfoDto userInfoDto = UserInfoDto.EntityToDto(user);

        return ResponseDto.ok(userInfoDto);
    }
}
