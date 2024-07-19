package com.example.kakao_mlms.repository;

import com.example.kakao_mlms.domain.Qna;
import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.QnaDtoWithImages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    Page<Qna> findByTitleContainingAndCategory(String title, Category category, Pageable pageable);
    Page<Qna> findByUser_Id(Long userId, Pageable pageable);
    Page<Qna> findByUser_IdAndTitleContainingAndCategory(Long userId, String title, Category category, Pageable pageable);

    Optional<Qna> findQnaById(Long id);
    Optional<Qna> findQnaByTitle(String title);
    Optional<Qna> findQnaByIdAndUser(Long id, User user);
    Page<Qna> findQnaByTitleContaining(String title, Pageable pageable);
}