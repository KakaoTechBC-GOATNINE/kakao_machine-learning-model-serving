package com.example.kakao_mlms.domain.type;

import lombok.Getter;

public enum Extension {
    JPEG("jpeg"),
    JPG(".jpg"),
    PNG(".png"),
    WEBP("webp");

    @Getter private final String value;

    Extension(String value) {
        this.value = value;
    }

}
