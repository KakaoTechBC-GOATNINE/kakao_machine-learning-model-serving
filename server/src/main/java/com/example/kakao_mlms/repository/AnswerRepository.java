package com.example.kakao_mlms.repository;

import com.example.kakao_mlms.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByQna_Id(Long qnaId);
}