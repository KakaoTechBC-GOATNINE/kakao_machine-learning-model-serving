package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.dto.SelfValidating;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class JwtTokenDto extends SelfValidating<JwtTokenDto> {
    @NotNull String accessToken;
    @NotNull String refreshToken;

    @Builder
    public JwtTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
