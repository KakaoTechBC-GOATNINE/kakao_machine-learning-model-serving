package com.example.kakao_mlms.dto.response;

import java.util.List;

public record ReviewDtoResponse(
        float latitude,
        float longitude,
        String keyword,
        List<Review> reviews
) {
    public static record Review(
            String storeName,
            String address,
            double score
    ) {
    }
}
