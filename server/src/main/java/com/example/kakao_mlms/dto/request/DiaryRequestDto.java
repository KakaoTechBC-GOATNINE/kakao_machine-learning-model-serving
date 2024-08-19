package com.example.kakao_mlms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


public record DiaryRequestDto(
        @JsonProperty("title")
        @NotNull
        @Size(min = 1)
        String title,
        @JsonProperty("content")
        @NotNull
        @Size(min = 1, max = 1000)
        String content,
        @JsonProperty("createdDate")
        @NotNull
        LocalDate createdDate) {


}
