package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.QnaDto;

import java.time.LocalDateTime;

public record QnaDtoResponse(Long id,
                             String title,
                             Category category,
                             LocalDateTime createdDate,
                             Boolean isAnswer,
                             Boolean isBlind,
                             UserDtoResponse user) {
    public static QnaDtoResponse from(QnaDto qnaDto) {
        return new QnaDtoResponse(qnaDto.id(),
                qnaDto.title(),
                qnaDto.category(),
                qnaDto.createdDate(),
                qnaDto.isAnswer(),
                qnaDto.isBlind(),
                UserDtoResponse.from(qnaDto.user()));
    }
}
