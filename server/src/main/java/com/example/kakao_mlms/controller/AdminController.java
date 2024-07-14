package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.dto.response.UserDto;
import com.example.kakao_mlms.exception.ResponseDto;
import com.example.kakao_mlms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseDto<Page<UserDto>> getAllUsers(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "nickname", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserDto> users = userService.searchUsers(name, pageable);
        return ResponseDto.ok(users);
    }
}
