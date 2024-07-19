package com.example.kakao_mlms.dto;

import com.example.kakao_mlms.domain.Qna;
import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.response.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.example.kakao_mlms.domain.Qna}
 */
public record QnaDto(
        Long id,
        String content,
        Category category,
        LocalDateTime createdDate,
        Boolean isAnswer,
        Boolean isBlind,
        UserDto user
) {
    public static QnaDto from(Qna qna) {
        return new QnaDto(qna.getId(),
                qna.getContent(),
                qna.getCategory(),
                qna.getCreatedDate(),
                qna.getIsAnswer(),
                qna.getIsBlind(),
                UserDto.from(qna.getUser()));
    }
}