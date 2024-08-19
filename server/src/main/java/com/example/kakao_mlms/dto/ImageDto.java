package com.example.kakao_mlms.dto;

import com.example.kakao_mlms.domain.Image;
import com.example.kakao_mlms.domain.Qna;
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
        QnaDto qna
) {
  public static ImageDto from(Image image) {
    return new ImageDto(
            image.getId(),
            image.getOriginName(),
            image.getUuidName(),
            image.getExtension(),
            QnaDto.from(image.getQna())
    );
  }

  public Image toEntity(Qna qna) {
    return Image.builder().originName(originName).uuidName(uuidName).extension(extension).qna(qna).build();
  }

  public static ImageDto of(String originName, String uuidName, Extension extension) {
    return new ImageDto(null, originName, uuidName, extension, null);
  }
}