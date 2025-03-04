package com.jerry.ff.controller;

import com.jerry.ff.model.vo.FileUploadResponse;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
@Tag(name = "文件上传", description = "文件上传相关接口")
public class FileUploadController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping("/image")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "上传图片", description = "上传图片文件")
    public ResponseResult<FileUploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error(400, "请选择要上传的文件");
        }

        // 校验文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseResult.error(400, "只能上传图片文件");
        }

        try {
            // 创建上传目录
            Path uploadPath = Paths.get(uploadDir, "images");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 处理文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            // 保存文件
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // 生成访问URL
            String fileUrl = "/uploads/images/" + newFilename;

            FileUploadResponse response = new FileUploadResponse(fileUrl, file.getSize());
            return ResponseResult.success(response);

        } catch (IOException e) {
            return ResponseResult.error("文件上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/video")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "上传视频", description = "上传视频文件，需要管理员权限")
    public ResponseResult<FileUploadResponse> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error(400, "请选择要上传的文件");
        }

        // 校验文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            return ResponseResult.error(400, "只能上传视频文件");
        }

        try {
            // 创建上传目录
            Path uploadPath = Paths.get(uploadDir, "videos");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 处理文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            // 保存文件
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // 生成访问URL
            String fileUrl = "/uploads/videos/" + newFilename;

            FileUploadResponse response = new FileUploadResponse(fileUrl, file.getSize());
            return ResponseResult.success(response);

        } catch (IOException e) {
            return ResponseResult.error("文件上传失败: " + e.getMessage());
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }
} 