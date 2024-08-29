package com.example.kakao_mlms.service;

import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.dto.request.ReviewDto;
import com.example.kakao_mlms.dto.response.ReviewDtoResponse;
import com.example.kakao_mlms.dto.response.ReviewDtoResponse.Review.ClusteredTerm;
import com.example.kakao_mlms.exception.CommonException;
import com.example.kakao_mlms.exception.ErrorCode;
import com.example.kakao_mlms.repository.UserRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    @Value("${ml.review-url}")
    private String ML_REVIEW_URL;
    private final UserRepository userRepository;

    private static final RestTemplate restTemplate = new RestTemplate();

    /// AI 이용
    @Transactional
    public ReviewDtoResponse getReviewResult(Long userId, ReviewDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.put("latitude", requestDto.latitude());
        body.put("longitude", requestDto.longitude());
        body.put("keyword", requestDto.keyword());

        HttpEntity<?> request = new HttpEntity<>(body.toJSONString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                ML_REVIEW_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        // JSON 파싱
        JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();

        // 응답에서 상태와 키워드 가져오기
        String status = jsonObject.get("status").getAsString();
        String location = jsonObject.get("keyword").getAsString();
        JsonArray rankedRestaurantArray = jsonObject.getAsJsonArray("ranked_resturant");

        List<ReviewDtoResponse.Review> reviews = new ArrayList<>();
        for (int i = 0; i < rankedRestaurantArray.size(); i++) {
            JsonObject restaurantData = rankedRestaurantArray.get(i).getAsJsonObject();
            String storeName = restaurantData.get("store_name").getAsString();
            String address = restaurantData.get("address").getAsString();
            double score = restaurantData.get("score").getAsDouble();

            // 클러스터링된 용어 가져오기
            JsonArray clusteredTermsArray = restaurantData.getAsJsonArray("clustered_terms");
            List<ClusteredTerm> clusteredTerms = new ArrayList<>();
            for (int j = 0; j < clusteredTermsArray.size(); j++) {
                JsonArray termData = clusteredTermsArray.get(j).getAsJsonArray();
                String term = termData.get(0).getAsString();
                int frequency = termData.get(1).getAsInt();
                clusteredTerms.add(new ClusteredTerm(term, frequency));
            }

            reviews.add(new ReviewDtoResponse.Review(storeName, address, score, clusteredTerms));
        }

        return new ReviewDtoResponse(requestDto.latitude(), requestDto.longitude(), location, reviews);
    }
}
