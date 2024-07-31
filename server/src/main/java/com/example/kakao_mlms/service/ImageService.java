package com.example.kakao_mlms.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    public List<String> uploadFiles(MultipartFile[] files) {
        List<String> filePaths = new ArrayList<>();

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : files) {
                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String fileExtension = StringUtils.getFilenameExtension(originalFileName);

                if (fileExtension == null || fileExtension.isEmpty()) {
                    fileExtension = "tmp"; // 기본 확장자 설정
                }

                String fileName = UUID.randomUUID().toString() + "." + fileExtension;
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                filePaths.add(filePath.toString());
            }

            return filePaths;
        } catch (IOException e) {
            log.error("파일 업로드 중 오류가 발생했습니다.", e);
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    public Resource downloadFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("파일을 찾을 수 없습니다: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("파일 경로에 오류가 있습니다: " + fileName, e);
        }
    }
}
