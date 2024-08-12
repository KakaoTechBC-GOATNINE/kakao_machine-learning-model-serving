package com.example.kakao_mlms.domain;

import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.dto.ImageDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
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
    private List<Image> images = new ArrayList<>();

    @Builder
    public Qna(String title, String content, Category category, Boolean isAnswer, Boolean isBlind, User user) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.isAnswer = isAnswer;
        this.isBlind = isBlind;
        this.user = user;
    }

    public void addImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Qna qna)) return false;
        return Objects.nonNull(this.getId()) && Objects.equals(this.getId(), qna.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    public void update(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}