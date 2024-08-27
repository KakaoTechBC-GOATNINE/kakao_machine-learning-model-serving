package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.AnswerDto;
import com.example.kakao_mlms.dto.QnaDtoWithImages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record QnaDtoWithImagesResponse(Long id,
                                       String title,
                                       String content,
                                       Category category,
                                       LocalDateTime createdDate,
                                       Boolean isAnswer,
                                       Boolean isBlind,
                                       UserDtoResponse user,
                                       List<ImageResponse> images,
                                       AnswerDtoResponse answer
) {
    public static QnaDtoWithImagesResponse from(QnaDtoWithImages qnaDtoWithImages, AnswerDto answerDto) {
        return new QnaDtoWithImagesResponse(qnaDtoWithImages.id(),
                qnaDtoWithImages.title(),
                qnaDtoWithImages.content(),
                qnaDtoWithImages.category(),
                qnaDtoWithImages.createdDate(),
                qnaDtoWithImages.isAnswer(),
                qnaDtoWithImages.isBlind(),
                UserDtoResponse.from(qnaDtoWithImages.user()),
                qnaDtoWithImages.images().stream()
                        .map(ImageResponse::from).toList(),
                Optional.ofNullable(answerDto)
                        .map(AnswerDtoResponse::from)
                        .orElse(null));
    }
}
