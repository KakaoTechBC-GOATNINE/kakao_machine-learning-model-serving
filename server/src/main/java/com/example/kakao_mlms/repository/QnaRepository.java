package com.example.kakao_mlms.repository;

import com.example.kakao_mlms.domain.Qna;
import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.Category;
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

    @Query("SELECT q FROM Qna q WHERE q.user.id = :userId AND q.category = :category AND LOWER(q.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Qna> findByUserIdAndTitleAndCategory(@Param("userId") Long userId, @Param("title") String title, @Param("category") Category category, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.isAnswer = :isAnswer AND q.category = :category AND LOWER(q.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Qna> findByIsAnswerAndTitleAndCategory(@Param("isAnswer") Boolean isAnswer, @Param("title") String title, @Param("category") Category category, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.user.id = :userId AND q.category = :category")
    Page<Qna> findByUserIdAndCategory(@Param("userId") Long userId, @Param("category") Category category, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.isAnswer = :isAnswer AND q.category = :category")
    Page<Qna> findByIsAnswerAndCategory(@Param("isAnswer") Boolean isAnswer, @Param("category") Category category, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.category = :category")
    Page<Qna> findByCategory(@Param("category") Category category, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.user.id = :userId AND LOWER(q.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Qna> findByUserIdAndTitle(@Param("userId") Long userId, @Param("title") String title, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.isAnswer = :isAnswer AND LOWER(q.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Qna> findByIsAnswerAndTitle(@Param("isAnswer") Boolean isAnswer, @Param("title") String title, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.category = :category AND LOWER(q.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Qna> findByTitleAndCategory(@Param("title") String title, @Param("category") Category category, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Qna> findByTitle(@Param("title") String title, Pageable pageable);


    Optional<Qna> findQnaById(Long id);
    Page<Qna> findByUserId(Long userId, Pageable pageable);
    Page<Qna> findByIsAnswer(Boolean isAnswer, Pageable pageable);
    Optional<Qna> findQnaByTitle(String title);
    Optional<Qna> findQnaByIdAndUser(Long id, User user);
    Page<Qna> findQnaByTitleContaining(String title, Pageable pageable);

    @Query(value = "UPDATE qnas set user_id = 1 where user_id = :userId", nativeQuery = true)
    @Modifying
    void updateQnaByUserId(@Param("userId") Long userId);
}