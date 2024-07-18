package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.annotation.UserId;
import com.example.kakao_mlms.dto.request.QnaRequestDto;
import com.example.kakao_mlms.exception.ResponseDto;
import com.example.kakao_mlms.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/qnas")
@RequiredArgsConstructor
public class QnaController {
    private final QnaService qnaService;

    @PostMapping("")
    public ResponseDto<?> createQna(@UserId Long id, @RequestBody QnaRequestDto requestDto) {
        return ResponseDto.ok(qnaService.createQna(id, requestDto));
    }

    @PatchMapping("/{qnaId}")
    public ResponseDto<?> updateQna(@UserId Long id, @PathVariable("qnaId") Long qnaId, @RequestBody QnaRequestDto requestDto) {
        return ResponseDto.ok(qnaService.updateQna(id, qnaId, requestDto));
    }

    @DeleteMapping("/{qnaId}")
    public ResponseDto<?> deleteQna(@UserId Long id, @PathVariable("qnaId") Long qnaId) {
        return ResponseDto.ok(qnaService.deleteQna(id, qnaId));
    }

    @GetMapping("")
    public ResponseDto<?> readQnaList(@RequestParam(name = "title", defaultValue = "", required = false) String title,
                                      @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseDto.ok(qnaService.readQnaList(title, pageable));
    }

    @GetMapping("/{qnaId}")
    public ResponseDto<?> readQnaDetail(@UserId Long id, @PathVariable("qnaId") Long qnaId) {
        return ResponseDto.ok(qnaService.readQnaDetail(qnaId));
    }

}
