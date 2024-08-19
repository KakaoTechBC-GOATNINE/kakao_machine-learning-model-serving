package com.example.kakao_mlms.repository;

import com.example.kakao_mlms.domain.Qna;
import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.QnaDtoWithImages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    Page<Qna> findByTitleContainingAndCategory(String title, Category category, Pageable pageable);
    Page<Qna> findByUser_IdOrIsBlind(Long userId, boolean isBlind, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE (q.user.id = :userId OR q.isBlind = :isBlind) AND q.title LIKE %:title% AND q.category = :category")
    Page<Qna> findByUserIdOrIsBlindAndTitleAndCategory(@Param("userId") Long userId, @Param("isBlind") boolean isBlind, @Param("title") String title, @Param("category") Category category, Pageable pageable);


    Optional<Qna> findQnaById(Long id);
    Optional<Qna> findQnaByTitle(String title);
    Optional<Qna> findQnaByIdAndUser(Long id, User user);
    Page<Qna> findQnaByTitleContaining(String title, Pageable pageable);

    @Query(value = "UPDATE qnas set user_id = 1 where user_id = :userId", nativeQuery = true)
    @Modifying
    void updateQnaByUserId(@Param("userId") Long userId);
}