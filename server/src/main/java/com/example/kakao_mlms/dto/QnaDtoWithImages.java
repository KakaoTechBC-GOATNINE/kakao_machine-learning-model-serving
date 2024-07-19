package com.example.kakao_mlms.dto;

import com.example.kakao_mlms.domain.Qna;
import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.response.UserDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.example.kakao_mlms.domain.Qna}
 */
public record QnaDtoWithImages(
        Long id,
        String content,
        Category category,
        LocalDateTime createdDate,
        Boolean isAnswer,
        Boolean isBlind,
        UserDto user,
        List<ImageDto> images
) {
  public static QnaDtoWithImages from (Qna qna) {
    return new QnaDtoWithImages(
            qna.getId(),
            qna.getContent(),
            qna.getCategory(),
            qna.getCreatedDate(),
            qna.getIsAnswer(),
            qna.getIsBlind(),
            UserDto.from(qna.getUser()),
            qna.getImages().stream().map(ImageDto::from).toList()
    );
  }
}