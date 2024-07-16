package com.example.kakao_mlms.service;

import com.example.kakao_mlms.domain.Qna;
import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.ERole;
import com.example.kakao_mlms.dto.PageInfo;
import com.example.kakao_mlms.dto.request.QnaRequestDto;
import com.example.kakao_mlms.dto.response.AllDto;
import com.example.kakao_mlms.dto.response.QnaDetailDto;
import com.example.kakao_mlms.dto.response.QnaListDto;
import com.example.kakao_mlms.exception.CommonException;
import com.example.kakao_mlms.exception.ErrorCode;
import com.example.kakao_mlms.repository.QnaRepository;
import com.example.kakao_mlms.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QnaService {
    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;

    public Boolean createQna(Long id, QnaRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        qnaRepository.save(Qna.builder()
                .title(requestDto.title())
                .content(requestDto.content())
                .category(requestDto.category())
                .user(user).build());

        return Boolean.TRUE;
    }

    public QnaDetailDto readQnaDetail(Long qnaId) {
        Qna qna = qnaRepository.findQnaById(qnaId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_QNA));

        return new QnaDetailDto(qna.getTitle(), qna.getContent(), qna.getCategory(), qna.getCreatedDate().toString());
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
}
