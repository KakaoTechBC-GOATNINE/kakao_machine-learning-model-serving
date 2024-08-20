package com.example.kakao_mlms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewDto(
        @JsonProperty("latitude")
        @NotNull
        float latitude,

        @JsonProperty("longitude")
        @NotNull
        float longitude,

        @JsonProperty("keyword")
        @NotNull
        String keyword
) {
}
