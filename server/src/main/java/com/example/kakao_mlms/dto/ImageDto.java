package com.example.kakao_mlms.dto;

import com.example.kakao_mlms.domain.Image;
import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.domain.type.Extension;

/**
 * DTO for {@link com.example.kakao_mlms.domain.Image}
 */
public record ImageDto(
        Long id,
        String originName,
        String uuidName,
        Extension extension,
        Category category,
        QnaDto qna
) {
  public static ImageDto from(Image image) {
    return new ImageDto(
            image.getId(),
            image.getOriginName(),
            image.getUuidName(),
            image.getExtension(),
            image.getCategory(),
            QnaDto.from(image.getQna())
    );
  }
}