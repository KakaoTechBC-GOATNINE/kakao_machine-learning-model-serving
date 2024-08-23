package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.annotation.UserId;
import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.response.QnaDtoResponse;
import com.example.kakao_mlms.dto.UserDto;
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
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(required = false, name = "name") String name,
            @PageableDefault(size = 10, sort = "nickname", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserDto> users = userService.searchUsers(name, pageable);
        return ResponseEntity.ok(users);
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

    @PostMapping("/qnas/{id}")
    public ResponseEntity<Long> replyQna(@PathVariable("id") Long id, @RequestBody String content, @UserId Long userId) {
        answerService.replyQna(userId, id, content);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}
