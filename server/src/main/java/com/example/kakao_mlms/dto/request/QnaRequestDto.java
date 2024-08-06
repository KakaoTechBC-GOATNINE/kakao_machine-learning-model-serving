package com.example.kakao_mlms.dto.request;

import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.QnaDto;
import com.example.kakao_mlms.dto.response.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        String category,

        @JsonProperty("isBlind")
        @NotNull
        Boolean isBlind) {

        public QnaDto toDto(UserDto userDto) {
                return QnaDto.of(title, content, Category.valueOf(category), isBlind, userDto);
        }

}
