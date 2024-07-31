package com.example.kakao_mlms.service;

import com.example.kakao_mlms.domain.Answer;
import com.example.kakao_mlms.domain.Qna;
import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.repository.AnswerRepository;
import com.example.kakao_mlms.repository.QnaRepository;
import com.example.kakao_mlms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {
    @InjectMocks private AnswerService sut;
    @Mock private QnaRepository qnaRepository;
    @Mock private UserRepository userRepository;
    @Mock private AnswerRepository answerRepository;

    @Test
    void replyQna() {
        // Given
        Long adminId = 1L;
        Long qnaId = 1L;
        given(qnaRepository.findById(qnaId)).willReturn(Optional.ofNullable(createQna()));
        given(userRepository.findById(adminId)).willReturn(Optional.ofNullable(createUser()));

        // When
        sut.replyQna(adminId, qnaId, "content");

        // Then
        then(qnaRepository).should().findById(qnaId);
        then(userRepository).should().findById(adminId);
        then(answerRepository).should().save(any(Answer.class));
    }

    private Qna createQna() {
        return Qna.builder().title("title").user(createUser()).build();
    }

    private User createUser() {
        return User.builder().serialId("tester").build();
    }

}