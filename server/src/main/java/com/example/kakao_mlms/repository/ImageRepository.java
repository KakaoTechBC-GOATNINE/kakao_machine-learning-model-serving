package com.example.kakao_mlms.repository;

import com.example.kakao_mlms.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}