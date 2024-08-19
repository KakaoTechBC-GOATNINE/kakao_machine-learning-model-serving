package com.example.kakao_mlms.controller;

import com.example.kakao_mlms.annotation.UserId;
import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.ImageDto;
import com.example.kakao_mlms.dto.QnaDto;
import com.example.kakao_mlms.dto.QnaDtoWithImages;
import com.example.kakao_mlms.dto.request.QnaRequestDto;
import com.example.kakao_mlms.dto.response.QnaDtoResponse;
import com.example.kakao_mlms.dto.response.QnaDtoWithImagesResponse;
import com.example.kakao_mlms.dto.response.UserDto;
import com.example.kakao_mlms.exception.ResponseDto;
import com.example.kakao_mlms.service.ImageService;
import com.example.kakao_mlms.service.QnaService;
import com.example.kakao_mlms.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/qnas")
@RequiredArgsConstructor
public class QnaController {
    private final UserService userService;
    private final QnaService qnaService;
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<Void> createQna(@UserId Long id,
                                    @RequestPart("qnaRequestDto") QnaRequestDto qnaRequestDto,
                                    @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        User user = userService.getUserInfo(id);
        List<ImageDto> imageDtos = imageService.uploadFiles(images);
        qnaService.createQna(qnaRequestDto.toDto(UserDto.from(user)), imageDtos);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<QnaDtoResponse>> getQnaList(@UserId Long id,
                             @RequestParam(required = false) String title,
                             @RequestParam(required = false) Category category,
                             @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<QnaDtoResponse> qnaResponse = qnaService.searchQnasByUser(title, id, category, pageable)
                .map(QnaDtoResponse::from);
        return ResponseEntity.ok(qnaResponse);
    }

    @GetMapping("/{qnaId}")
    public ResponseEntity<?> getQna(@UserId Long id, @PathVariable("qnaId") Long qnaId) {
        QnaDtoWithImagesResponse qnaWithImagesResponse = QnaDtoWithImagesResponse.from(qnaService.getQnaWithImages(qnaId));
        if (qnaWithImagesResponse.user().id().longValue() == id.longValue()) {
            return ResponseEntity.ok(qnaWithImagesResponse);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/image/{filename}")
    public Resource downloadImage(@PathVariable("filename") String storeFilename) throws MalformedURLException {
         return imageService.downloadImage(storeFilename);
    }


    @PutMapping("/{qnaId}")
    public ResponseEntity<Long> updateQna(@UserId Long id,
                                    @PathVariable("qnaId") Long qnaId,
                                    @RequestPart QnaRequestDto qnaRequestDto,
                                    @RequestPart(required = false) List<MultipartFile> images) {
        User user = userService.getUserInfo(id);
        List<ImageDto> imageDtos = imageService.uploadFiles(images);
        QnaDto qnaDto = qnaRequestDto.toDto(UserDto.from(user), qnaId);
        qnaService.updateQna(id, qnaDto, imageDtos);
        return ResponseEntity.ok().body(qnaId);
    }

    @DeleteMapping("/{qnaId}")
    public ResponseDto<?> deleteQna(@UserId Long id, @PathVariable("qnaId") Long qnaId) {
        return ResponseDto.ok(qnaService.deleteQna(id, qnaId));
    }

//    @GetMapping("")
//    public ResponseDto<?> readQnaList(@RequestParam(name = "title", defaultValue = "", required = false) String title,
//                                      @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {
//        return ResponseDto.ok(qnaService.readQnaList(title, pageable));
//    }

}
