package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.response.QnaDtoResponse;
import com.example.kakao_mlms.dto.response.UserDto;
import com.example.kakao_mlms.exception.ResponseDto;
import com.example.kakao_mlms.security.CustomUserDetails;
import com.example.kakao_mlms.service.AnswerService;
import com.example.kakao_mlms.service.QnaService;
import com.example.kakao_mlms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final QnaService qnaService;
    private final AnswerService answerService;

    @GetMapping("/users")
    public ResponseDto<Page<UserDto>> getAllUsers(
            @RequestParam(required = false, name = "name") String name,
            @PageableDefault(size = 10, sort = "nickname", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserDto> users = userService.searchUsers(name, pageable);
        return ResponseDto.ok(users);
    }

    @GetMapping("/qnas")
    public ResponseDto<Page<QnaDtoResponse>> getAllQnas(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Category Category,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<QnaDtoResponse> qnaResponse = qnaService.searchQnas(title, Category, pageable)
                .map(QnaDtoResponse::from);
        return ResponseDto.ok(qnaResponse);
    }

    @PostMapping("/qnas/{id}")
    public ResponseDto<Long> replyQna(
            @PathVariable Long id,
            String content,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ) {
        answerService.replyQna(customUserDetails.getId(), id, content);
        return ResponseDto.created(id);
    }
}
