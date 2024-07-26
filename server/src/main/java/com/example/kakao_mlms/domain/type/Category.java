package com.example.kakao_mlms.domain.type;

import lombok.Getter;

public enum Category {
    GENERAL("일반 질문"),
    ACCOUNT("계정 관련"),
    TECH_SUPPORT("기술 지원"),
    OTHER("기타");

    @Getter private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

}
