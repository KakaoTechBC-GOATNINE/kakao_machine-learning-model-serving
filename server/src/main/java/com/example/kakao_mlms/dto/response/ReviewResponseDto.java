package com.example.kakao_mlms.dto.response;

public record ReviewResponseDto(
    float latitude,
    float longitude,
    String keyword
) {
}
