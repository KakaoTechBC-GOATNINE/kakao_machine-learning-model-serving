package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.dto.UserDto;

public record UserDtoResponse(
        Long id,
        String serialId,
        String nickname
) {
    public static UserDtoResponse from(UserDto userDto) {
        return new UserDtoResponse(userDto.id(),
                userDto.serialId(),
                userDto.nickname());
    }
}
