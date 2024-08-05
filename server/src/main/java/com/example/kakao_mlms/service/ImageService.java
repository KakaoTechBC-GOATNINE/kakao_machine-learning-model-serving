package com.example.kakao_mlms.service;

import com.example.kakao_mlms.domain.type.Extension;
import com.example.kakao_mlms.dto.ImageDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<ImageDto> uploadFiles(List<MultipartFile> images) {
        if (images.isEmpty()) {
            return null;
        }

        List<ImageDto> imageDtos = new ArrayList<>();

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : images) {
                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String fileExtension = StringUtils.getFilenameExtension(originalFileName);

                if (fileExtension == null || fileExtension.isEmpty()) {
                    fileExtension = "png"; // 기본 확장자 설정
                }

                String storeFileName = UUID.randomUUID() + "." + fileExtension;
                String filePath = uploadDir + storeFileName;
                file.transferTo(new File(filePath));

                imageDtos.add(ImageDto.of(originalFileName, storeFileName, Extension.valueOf(fileExtension.toUpperCase())));
            }

            return imageDtos;
        } catch (IOException e) {
            log.error("파일 업로드 중 오류가 발생했습니다.", e);
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    public Resource downloadImage(String storeFilename) throws MalformedURLException {
        return new FileUrlResource(uploadDir + storeFilename);
    }
}
