//package com.example.kakao_mlms.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/images")
//public class ImageController {
//
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    @PostMapping("/upload")
//    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
//        List<String> fileDownloadUrls = new ArrayList<>();
//
//        try {
//            Path uploadPath = Paths.get(uploadDir);
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            for (MultipartFile file : files) {
//                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
//                String fileExtension = StringUtils.getFilenameExtension(originalFileName);
//                String fileName = UUID.randomUUID().toString() + "." + fileExtension;
//
//                Path filePath = uploadPath.resolve(fileName);
//                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                        .path("/api/images/download/")
//                        .path(fileName)
//                        .toUriString();
//
//                fileDownloadUrls.add(fileDownloadUri);
//            }
//
//            return new ResponseEntity<>(fileDownloadUrls, HttpStatus.OK);
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("/download/{fileName:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {
//        try {
//            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if (resource.exists()) {
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
//                        .body(resource);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}
