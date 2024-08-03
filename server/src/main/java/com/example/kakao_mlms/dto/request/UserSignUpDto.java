package com.example.kakao_mlms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserSignUpDto(@JsonProperty("serialId")
                            @NotNull
                            @NotBlank
                            String serialId,
                            @JsonProperty("password")
                            @NotNull
                            @NotBlank
                            String password,
                            @JsonProperty("nickname")
                            @NotNull
                            @NotBlank
                            String nickname) {

}
