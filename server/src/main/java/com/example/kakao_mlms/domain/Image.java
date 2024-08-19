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
@Table(name = "images")
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "qna_id", foreignKey = @ForeignKey(name = "fk_image_qna_id"))
    private Qna qna;

    @Builder
    private Image(String originName, String uuidName, Extension extension, Qna qna) {
        this.originName = originName;
        this.uuidName = uuidName;
        this.extension = extension;
        this.qna = qna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image image)) return false;
        return Objects.nonNull(this.getId()) && Objects.equals(this.getId(), image.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }
}
