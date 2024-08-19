package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.annotation.UserId;
import com.example.kakao_mlms.dto.request.ReviewDto;
import com.example.kakao_mlms.exception.ResponseDto;
import com.example.kakao_mlms.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/ai")
    public ResponseDto<?> getReviewResult(@UserId Long id, @RequestBody ReviewDto requestDto) {
        return ResponseDto.ok(reviewService.getReviewResult(id, requestDto));
    }
}
