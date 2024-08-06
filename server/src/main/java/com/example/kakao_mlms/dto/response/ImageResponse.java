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
        String uuidName,
        Extension extension
) {
    public static ImageResponse from(ImageDto imageDto) {
        return new ImageResponse(
                imageDto.originName(),
                imageDto.uuidName(),
                imageDto.extension()
        );
    }

}
