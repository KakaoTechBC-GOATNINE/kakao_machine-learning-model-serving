package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.domain.type.EProvider;
import com.example.kakao_mlms.domain.type.ERole;
import com.example.kakao_mlms.dto.UserDto;

import java.time.LocalDate;

public record AdminUserInfoDto(Long id,
                               String serialId,
                               EProvider provider,
                               ERole role,
                               LocalDate createdDate,
                               String nickname,
                               Boolean isLogin
) {
    public static AdminUserInfoDto from(UserDto userDto) {
        return new AdminUserInfoDto(userDto.id(),
                userDto.serialId(),
                userDto.provider(),
                userDto.role(),
                userDto.createdDate(),
                userDto.nickname(),
                userDto.isLogin());
    }
}
