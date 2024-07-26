package com.example.kakao_mlms.domain;

import com.example.kakao_mlms.domain.type.Category;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "qnas")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = 10000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "is_answer", nullable = false)
    private Boolean isAnswer;

    @Column(name = "is_blind", nullable = false)
    private Boolean isBlind;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_qna_user_id"))
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL)
    private List<Image> images;

    @Builder
    private Qna(String title, String content, Category category, User user) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qna qna = (Qna) o;
        return Objects.equals(id, qna.id) && Objects.equals(content, qna.content) && category == qna.category && Objects.equals(createdDate, qna.createdDate) && Objects.equals(isAnswer, qna.isAnswer) && Objects.equals(isBlind, qna.isBlind) && Objects.equals(user, qna.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, category, createdDate, isAnswer, isBlind, user);
    }

    public void update(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = Category.valueOf(category);
    }
}