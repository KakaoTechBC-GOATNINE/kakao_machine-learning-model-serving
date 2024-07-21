package com.example.kakao_mlms.domain;

import com.example.kakao_mlms.domain.type.Category;
import com.example.kakao_mlms.domain.type.Extension;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Getter
@Entity
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(name = "uuid_name", nullable = false)
    private String uuidName;

    @Enumerated(EnumType.STRING)
    @Column(name = "extension", nullable = false)
    private Extension extension;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "qna_id", foreignKey = @ForeignKey(name = "fk_image_qna_id"))
    private Qna qna;

    @Builder
    private Image(String originName, String uuidName, Extension extension, Category category, Qna qna) {
        this.originName = originName;
        this.uuidName = uuidName;
        this.extension = extension;
        this.category = category;
        this.qna = qna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(id, image.id) && Objects.equals(originName, image.originName) && Objects.equals(uuidName, image.uuidName) && extension == image.extension && category == image.category && Objects.equals(qna, image.qna);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, originName, uuidName, extension, category, qna);
    }
}