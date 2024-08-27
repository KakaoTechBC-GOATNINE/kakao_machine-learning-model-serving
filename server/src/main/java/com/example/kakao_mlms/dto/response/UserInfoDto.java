package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.EProvider;
import com.example.kakao_mlms.domain.type.ERole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfoDto {
    @JsonProperty("serialId")
    @NotNull
    private final String serialId;

    @JsonProperty("nickname")
    @NotNull
    private final String nickname;

    @JsonProperty("eRole")
    @NotNull
    private final ERole eRole;

    @JsonProperty("eProvider")
    @NotNull
    private final EProvider eProvider;


    public static UserInfoDto EntityToDto(User user) {

        return new UserInfoDto(user.getSerialId(), user.getNickname(), user.getRole(), user.getProvider());
    }
}
