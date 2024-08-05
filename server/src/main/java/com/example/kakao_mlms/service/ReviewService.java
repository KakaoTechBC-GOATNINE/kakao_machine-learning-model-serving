package com.example.kakao_mlms.service;

import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.dto.request.ReviewDto;
import com.example.kakao_mlms.dto.response.ReviewResponseDto;
import com.example.kakao_mlms.exception.CommonException;
import com.example.kakao_mlms.exception.ErrorCode;
import com.example.kakao_mlms.repository.UserRepository;
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

import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    @Value("http://localhost:8000/api/v1/stores/ai")
    private String ML_REVIEW_URL;
    private final UserRepository userRepository;

    private static final RestTemplate restTemplate = new RestTemplate();


    /// AI 이용
    @Transactional
    public ReviewResponseDto getReviewResult(Long userId, ReviewDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.put("latitude", requestDto.latitude());
        body.put("longitude", requestDto.longitude());
        body.put("keyword", requestDto.keyword());


        HttpEntity<?> request = new HttpEntity<String>(body.toJSONString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                ML_REVIEW_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        // JSON 파싱
        JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();

        // 각 키 값 가져오기
        String status = jsonObject.get("status").getAsString();
        String keyword = jsonObject.get("keyword").getAsString();
        String result = jsonObject.get("result").getAsString();


        return new ReviewResponseDto(requestDto.latitude(), requestDto.longitude(), keyword);
    }


}
