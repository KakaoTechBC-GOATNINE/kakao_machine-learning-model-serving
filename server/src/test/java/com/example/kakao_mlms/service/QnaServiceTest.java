package com.example.kakao_mlms.service;

import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.QnaDtoWithImages;
import com.example.kakao_mlms.repository.QnaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

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
        Page<QnaDtoWithImages> qnaDtoWithImages = sut.searchQnas(null, null, pageable);

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
        Page<QnaDtoWithImages> qnaDtoWithImages = sut.searchQnas(title, category, pageable);

        // Then
        assertThat(qnaDtoWithImages).isEmpty();
        then(qnaRepository).should().findByTitleContainingAndCategory(title, category, pageable);

    }

    @Test
    void searchQnasByUser() {
        // Given
        Long userId = 1L;
        Pageable pageable = Pageable.ofSize(5);
        given(qnaRepository.findByUser_Id(userId, pageable)).willReturn(Page.empty(pageable));

        // When
        Page<QnaDtoWithImages> qnaDtoWithImages = sut.searchQnasByUser(null, userId, null, pageable);

        // Then
        assertThat(qnaDtoWithImages).isEmpty();
        then(qnaRepository).should().findByUser_Id(userId, pageable);

    }

    @Test
    void searchQnasByUserWithKeyword() {
        // Given
        Long userId = 1L;
        String title = "title";
        Category category = Category.ACCOUNT;
        Pageable pageable = Pageable.ofSize(5);
        given(qnaRepository.findByUser_IdAndTitleContainingAndCategory(userId, title, category, pageable)).willReturn(Page.empty(pageable));

        // When
        Page<QnaDtoWithImages> qnaDtoWithImages = sut.searchQnasByUser(title, userId, category, pageable);

        // Then
        assertThat(qnaDtoWithImages).isEmpty();
        then(qnaRepository).should().findByUser_IdAndTitleContainingAndCategory(userId, title, category, pageable);

    }
}