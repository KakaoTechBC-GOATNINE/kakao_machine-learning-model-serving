package com.example.kakao_mlms.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Getter
@DynamicUpdate
@Table(name = "qnas")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    //카테고리는 ENUM으로 정해놓을껀지?
    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDate createdDate;

    /* Relation Child Mapping */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //이미지 해야함

    @Builder
    public Qna(String title, String content, User user, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
        this.createdDate = LocalDate.now();
    }

    public void update(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
