package com.example.kakao_mlms.service;

import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.QnaDto;
import com.example.kakao_mlms.repository.QnaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {
    @InjectMocks private QnaService sut;
    @Mock private QnaRepository qnaRepository;

    @Test
    void searchQnas() {
        // Given
        Pageable pageable = Pageable.ofSize(5);
        given(qnaRepository.findAll(pageable)).willReturn(Page.empty(pageable));

        // When
        Page<QnaDto> qnaDtoWithImages = sut.searchQnas(null, null, pageable);

        // Then
        assertThat(qnaDtoWithImages).isEmpty();
        then(qnaRepository).should().findAll(pageable);
    }

    @Test
    void searchQnasWithKeyword() {
        // Given
        String title = "title";
        Category category = Category.ACCOUNT;
        Pageable pageable = Pageable.ofSize(5);
        given(qnaRepository.findByTitleContainingAndCategory(title, category, pageable)).willReturn(Page.empty(pageable));

        // When
        Page<QnaDto> qnaDtoWithImages = sut.searchQnas(title, category, pageable);

        // Then
        assertThat(qnaDtoWithImages).isEmpty();
        then(qnaRepository).should().findByTitleContainingAndCategory(title, category, pageable);

    }

    @Test
    void searchQnasByUser() {
        // Given
        Long userId = 1L;
        Boolean isBlind = false;
        Pageable pageable = Pageable.ofSize(5);
        given(qnaRepository.findByUser_IdOrIsBlind(userId, isBlind, pageable)).willReturn(Page.empty(pageable));

        // When
        Page<QnaDto> qnaDtoWithImages = sut.searchQnasByUser(null, userId, null, pageable);

        // Then
        assertThat(qnaDtoWithImages).isEmpty();
        then(qnaRepository).should().findByUser_IdOrIsBlind(userId, isBlind, pageable);

    }

    @Test
    void searchQnasByUserWithKeyword() {
        // Given
        Long userId = 1L;
        String title = "title";
        Boolean isBlind = false;
        Category category = Category.ACCOUNT;
        Pageable pageable = Pageable.ofSize(5);
        given(qnaRepository.findByUserIdAndTitleAndCategory(userId, isBlind, title, category, pageable)).willReturn(Page.empty(pageable));

        // When
        Page<QnaDto> qnaDtoWithImages = sut.searchQnasByUser(title, userId, category, pageable);

        // Then
        assertThat(qnaDtoWithImages).isEmpty();
        then(qnaRepository).should().findByUserIdAndTitleAndCategory(userId, isBlind, title, category, pageable);

    }
}