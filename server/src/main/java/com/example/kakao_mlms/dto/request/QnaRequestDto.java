package com.example.kakao_mlms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record QnaRequestDto(
        @JsonProperty("title")
        @NotNull
        @Size(min = 1, max = 50)
        String title,

        @JsonProperty("content")
        @NotNull
        @Size(min = 1, max = 1000)
        String content,

        @JsonProperty("category")
        @NotNull
        String category) {
}
