package com.example.kakao_mlms.repository;

import com.example.kakao_mlms.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}