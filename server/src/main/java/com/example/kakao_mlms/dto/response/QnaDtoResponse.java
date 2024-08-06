package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.QnaDto;
import com.example.kakao_mlms.dto.QnaDtoWithImages;

import java.time.LocalDateTime;

public record QnaDtoResponse(Long id,
                             String content,
                             Category category,
                             LocalDateTime createdDate,
                             Boolean isAnswer,
                             Boolean isBlind,
                             UserDto user) {
    public static QnaDtoResponse from(QnaDto qnaDto) {
        return new QnaDtoResponse(qnaDto.id(),
                qnaDto.content(),
                qnaDto.category(),
                qnaDto.createdDate(),
                qnaDto.isAnswer(),
                qnaDto.isBlind(),
                qnaDto.user());
    }
}
