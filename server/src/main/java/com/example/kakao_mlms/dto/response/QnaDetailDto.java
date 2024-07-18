package com.example.kakao_mlms.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record QnaDetailDto(
        @JsonProperty("title")
        @NotNull
        @Size(min = 1)
        String title,

        @JsonProperty("content")
        @NotNull
        @Size(min = 1, max = 1000)
        String content,

        @JsonProperty("category")
        @NotNull
        String category,

        @JsonProperty("date")
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        String date) {
}
