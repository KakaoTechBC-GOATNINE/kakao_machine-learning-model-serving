package com.example.kakao_mlms.service;

import com.example.kakao_mlms.domain.Image;
import com.example.kakao_mlms.domain.Qna;
import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.domain.type.ERole;
import com.example.kakao_mlms.dto.ImageDto;
import com.example.kakao_mlms.dto.PageInfo;
import com.example.kakao_mlms.dto.QnaDto;
import com.example.kakao_mlms.dto.QnaDtoWithImages;
import com.example.kakao_mlms.dto.request.QnaRequestDto;
import com.example.kakao_mlms.dto.response.*;
import com.example.kakao_mlms.exception.CommonException;
import com.example.kakao_mlms.exception.ErrorCode;
import com.example.kakao_mlms.repository.QnaRepository;
import com.example.kakao_mlms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaService {
    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;

    public void createQna(QnaDto qnaDto, List<ImageDto> imageDtos) {
        Qna qna = qnaDto.toEntity();
        if(imageDtos != null){
            List<Image> images = imageDtos.stream()
                    .map(imageDto -> imageDto.toEntity(qna)).toList();
            qna.addImages(images);
        }
        qnaRepository.save(qna);
    }

    @Transactional(readOnly = true)
    public QnaDtoWithImages getQnaWithImages(Long qnaId) {
        return qnaRepository.findQnaById(qnaId)
                .map(QnaDtoWithImages::from)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_QNA));
    }

    public AllDto<Object> readQnaList(String title, Pageable pageable) {
        Page<Qna> qnas = qnaRepository.findQnaByTitleContaining(title, pageable);

        PageInfo pageInfo = PageInfo.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements((int) qnas.getTotalElements())
                .totalPages(qnas.getTotalPages())
                .build();

        List<QnaListDto> qnaListDtos = qnas.stream()
                .map(QnaListDto::EntityToDto)
                .toList();

        return AllDto.builder()
                .dataList(qnaListDtos)
                .pageInfo(pageInfo)
                .build();
    }

    public Boolean updateQna(Long id, Long qnaId, QnaRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Qna qna = qnaRepository.findQnaByIdAndUser(qnaId, user)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_QNA));

        qna.update(requestDto.title(), requestDto.content(), requestDto.category());

        return Boolean.TRUE;
    }

    public Boolean deleteQna(Long id, Long qnaId) {
        Qna qna = null;
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        if (user.getRole() != ERole.ADMIN)
            qna = qnaRepository.findQnaByIdAndUser(qnaId, user)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_QNA));
        else
            qna = qnaRepository.findQnaById(qnaId)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_QNA));

        qnaRepository.delete(qna);

        return Boolean.TRUE;
    }

    @Transactional(readOnly = true)
    public Page<QnaDto> searchQnas(String title, Category category, Pageable pageable) {
        if (StringUtils.hasText(title)) {
            return qnaRepository.findByTitleContainingAndCategory(title, category, pageable).map(QnaDto::from);
        }
        return qnaRepository.findAll(pageable).map(QnaDto::from);
    }

    @Transactional(readOnly = true)
    public Page<QnaDto> searchQnasByUser(String title, Long userId, Category category, Pageable pageable) {
        if (StringUtils.hasText(title)) {
            return qnaRepository.findByUserIdOrIsBlindAndTitleAndCategory(userId, false, title, category, pageable).map(QnaDto::from);
        }
        return qnaRepository.findByUser_IdOrIsBlind(userId, false, pageable).map(QnaDto::from);
    }
}