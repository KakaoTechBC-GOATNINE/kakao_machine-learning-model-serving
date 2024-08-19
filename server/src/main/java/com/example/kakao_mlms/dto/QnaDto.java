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
        String title,
        String content,
        Category category,
        LocalDateTime createdDate,
        Boolean isAnswer,
        Boolean isBlind,
        UserDto user
) {
    public static QnaDto from(Qna qna) {
        return new QnaDto(qna.getId(),
                qna.getTitle(),
                qna.getContent(),
                qna.getCategory(),
                qna.getCreatedDate(),
                qna.getIsAnswer(),
                qna.getIsBlind(),
                UserDto.from(qna.getUser()));
    }

    public static QnaDto of(String title, String content, Category category, Boolean isBlind, UserDto user) {
        return new QnaDto(null, title, content, category, null, false, isBlind, user);
    }

    public static QnaDto of(Long id, String title, String content, Category category, Boolean isBlind, UserDto user) {
        return new QnaDto(id, title, content, category, null, false, isBlind, user);
    }

    public static QnaDto of() {
        return QnaDto.of(null, null, null, null, null);
    }

    public Qna toEntity() {
        return Qna.builder().title(title).content(content).category(category).isBlind(isBlind).isAnswer(isAnswer).user(user.toEntity()).build();
    }
}