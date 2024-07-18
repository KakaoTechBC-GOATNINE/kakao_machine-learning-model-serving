package com.example.kakao_mlms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record AuthSignUpDto(@JsonProperty("serialId")
                            @NotNull
                            @NotBlank
                            String serialId,
                            @JsonProperty("nickname")
                            @NotNull
                            @NotBlank
                            String nickname,
                            @JsonProperty("phoneNumber")
                            @NotNull
                            @NotBlank
                            String phoneNumber) {
}
