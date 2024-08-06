package com.example.kakao_mlms.service;

import com.example.kakao_mlms.domain.Answer;
import com.example.kakao_mlms.domain.Qna;
import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.repository.AnswerRepository;
import com.example.kakao_mlms.repository.QnaRepository;
import com.example.kakao_mlms.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {
    private final UserRepository userRepository;
    private final QnaRepository qnaRepository;
    private final AnswerRepository answerRepository;


    public void replyQna(Long adminId, Long qnaId, String content) {
        User adminUser = userRepository.findById(adminId).orElseThrow(EntityNotFoundException::new);
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(EntityNotFoundException::new);

        Answer answer = Answer.builder().content(content).qna(qna).user(adminUser).build();

        answerRepository.save(answer);
    }
}