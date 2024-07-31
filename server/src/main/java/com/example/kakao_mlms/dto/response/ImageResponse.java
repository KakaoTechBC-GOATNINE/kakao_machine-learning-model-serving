package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.constant.Constants;
import com.example.kakao_mlms.domain.type.Extension;
import com.example.kakao_mlms.dto.ImageDto;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public record ImageResponse(
        String originName,
        Extension extension,
        String encodedImage
) {
    public static ImageResponse from(ImageDto imageDto) {
        return new ImageResponse(
                imageDto.originName(),
                imageDto.extension(),
                readAndEncodeImage(imageDto)
        );
    }

    private static String readAndEncodeImage(ImageDto imageDto) {
        String imagePath = Constants.IMAGE_FILE_PATH_LOCAL + imageDto.uuidName() + imageDto.extension();
        File file = new File(imagePath);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] bytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            log.error("image file read error : {}", e.getMessage());
        }
        return null;
    }
}
