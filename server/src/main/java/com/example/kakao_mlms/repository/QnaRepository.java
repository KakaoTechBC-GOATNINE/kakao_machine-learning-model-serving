package com.example.kakao_mlms.repository;

import com.example.kakao_mlms.domain.Qna;
import com.example.kakao_mlms.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {
    Optional<Qna> findQnaById(Long id);

    Optional<Qna> findQnaByTitle(String title);

    Optional<Qna> findQnaByIdAndUser(Long id, User user);

    Page<Qna> findQnaByTitleContaining(String title, Pageable pageable);
}
