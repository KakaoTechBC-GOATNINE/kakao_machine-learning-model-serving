package com.example.kakao_mlms.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "answers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 10000)
    private String content;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "qna_id", foreignKey = @ForeignKey(name = "fk_answer_qna_id"))
    private Qna qna;

    @ManyToOne(optional = false)
    @JoinColumn(name = "admin_id", foreignKey = @ForeignKey(name = "fk_answer_admin_id"))
    private User user;

    @Builder
    private Answer(String content, Qna qna, User user) {
        this.content = content;
        this.qna = qna;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id) && Objects.equals(content, answer.content) && Objects.equals(createdDate, answer.createdDate) && Objects.equals(qna, answer.qna) && Objects.equals(user, answer.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, createdDate, qna, user);
    }
}
