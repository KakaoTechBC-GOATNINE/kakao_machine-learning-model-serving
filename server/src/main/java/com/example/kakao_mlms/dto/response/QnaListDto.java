package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.domain.Qna;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record QnaListDto(
        @JsonProperty("id")
        @NotNull
        Long id,

        @JsonProperty("title")
        @NotNull
        String title,

        @JsonProperty("content")
        @NotNull
        String content,

        @JsonProperty("category")
        @NotNull
        String category,

        @JsonProperty("date")
        @NotNull
        String date
        ) {

        public static QnaListDto EntityToDto(Qna qna) {
                return new QnaListDto(qna.getId(), qna.getTitle(),
                        qna.getContent(), qna.getCategory(),
                        qna.getCreatedDate().toString());
        }
}
