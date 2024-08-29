package com.example.kakao_mlms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewDtoResponse {
    private double latitude;
    private double longitude;
    private String keyword;
    private List<Review> reviews;

    @Getter
    @AllArgsConstructor
    public static class Review {
        private String storeName;
        private String address;
        private double score;
        private List<ClusteredTerm> clusteredTerms;

        @Getter
        @AllArgsConstructor
        public static class ClusteredTerm {
            private String term;
            private int frequency;
        }
    }
}
