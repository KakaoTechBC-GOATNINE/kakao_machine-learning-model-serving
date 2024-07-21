package com.example.kakao_mlms.dto;

import com.example.kakao_mlms.domain.Answer;
import com.example.kakao_mlms.dto.response.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.example.kakao_mlms.domain.Answer}
 */
public record AnswerDto(
        Long id,
        String content,
        LocalDateTime createdDate,
        QnaDto qna,
        UserDto user
) {
  public static AnswerDto from(Answer answer) {
    return new AnswerDto(
            answer.getId(),
            answer.getContent(),
            answer.getCreatedDate(),
            QnaDto.from(answer.getQna()),
            UserDto.from(answer.getUser())
    );
  }
}