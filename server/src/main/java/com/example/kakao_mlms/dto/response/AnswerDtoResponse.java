package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.dto.AnswerDto;

import java.time.LocalDateTime;

public record AnswerDtoResponse(Long id,
                                String content,
                                LocalDateTime createdDate) {
    public static AnswerDtoResponse from(AnswerDto answerDto) {
        return new AnswerDtoResponse(
                answerDto.id(),
                answerDto.content(),
                answerDto.createdDate()
        );
    }
}
