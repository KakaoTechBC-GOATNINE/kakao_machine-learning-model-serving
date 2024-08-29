package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.annotation.UserId;
import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.response.AdminUserInfoDto;
import com.example.kakao_mlms.dto.response.QnaDtoResponse;
import com.example.kakao_mlms.dto.UserDto;
import com.example.kakao_mlms.dto.response.QnaDtoWithImagesResponse;
import com.example.kakao_mlms.exception.ResponseDto;
import com.example.kakao_mlms.service.AnswerService;
import com.example.kakao_mlms.service.QnaService;
import com.example.kakao_mlms.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final QnaService qnaService;
    private final AnswerService answerService;

    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserInfoDto>> getAllUsers(
            @RequestParam(required = false, name = "name") String name,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AdminUserInfoDto> adminUserInfos = userService.searchUsers(name, pageable).map(AdminUserInfoDto::from);
        return ResponseEntity.ok(adminUserInfos);
    }

    @GetMapping("/qnas")
    public ResponseEntity<Page<QnaDtoResponse>> getAllQnas(
            @RequestParam(required = false, name = "notAnswered") Boolean notAnswered,
            @RequestParam(required = false, name = "title") String title,
            @RequestParam(required = false, name = "category") Category category,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("notAnswered = {}, title = {}, category = {}, pageable = {}", notAnswered, title, category, pageable);
        Page<QnaDtoResponse> qnaResponse = qnaService.searchQnas(title, category, notAnswered, pageable)
                .map(QnaDtoResponse::from);
        return ResponseEntity.ok(qnaResponse);
    }

    @GetMapping("/qnas/{qnaId}")
    public ResponseEntity<?> getQna(@UserId Long id, @PathVariable("qnaId") Long qnaId) {
        QnaDtoWithImagesResponse qnaWithImagesResponse =
                QnaDtoWithImagesResponse.from(qnaService.getQnaWithImages(qnaId), answerService.getAnswer(qnaId), id);
        return ResponseEntity.ok(qnaWithImagesResponse);
    }

    @PostMapping("/qnas/{qnaId}")
    public ResponseEntity<Long> replyQna(@PathVariable("qnaId") Long id, @RequestBody String content, @UserId Long userId) {
        answerService.replyQna(userId, id, content);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @DeleteMapping("/qnas/{qnaId}")
    public ResponseDto<?> deleteQna(@UserId Long id, @PathVariable("qnaId") Long qnaId) {
        return ResponseDto.ok(qnaService.deleteQna(id, qnaId));
    }
}
