package com.example.kakao_mlms.dto.response;

import java.util.List;

public record ReviewDtoResponse(
        float latitude,
        float longitude,
        String location,
        List<Review> reviews
) {
    public static record Review(
            String storeName,
            double rating,
            String address,
            List<String> comments
    ) {
    }
}
