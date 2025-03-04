package com.jerry.ff.controller;

import com.jerry.ff.model.dto.DirectorDTO;
import com.jerry.ff.model.vo.DirectorVO;
import com.jerry.ff.service.DirectorService;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "导演管理", description = "导演相关接口")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping("/api/directors")
    @Operation(summary = "获取导演列表", description = "分页获取所有导演")
    public ResponseResult<Page<DirectorVO>> getAllDirectors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseResult.success(directorService.getAllDirectors(pageable));
    }

    @GetMapping("/api/directors/{id}")
    @Operation(summary = "获取导演详情", description = "根据ID获取导演详情")
    public ResponseResult<DirectorVO> getDirector(@PathVariable Long id) {
        return ResponseResult.success(directorService.getDirector(id));
    }

    @PostMapping("/api/admin/directors")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "创建导演", description = "创建新导演，需要管理员权限")
    public ResponseResult<DirectorVO> createDirector(@Valid @RequestBody DirectorDTO directorDTO) {
        return ResponseResult.success(directorService.createDirector(directorDTO));
    }

    @PutMapping("/api/admin/directors/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新导演", description = "更新导演信息，需要管理员权限")
    public ResponseResult<DirectorVO> updateDirector(
            @PathVariable Long id,
            @Valid @RequestBody DirectorDTO directorDTO) {
        
        return ResponseResult.success(directorService.updateDirector(id, directorDTO));
    }

    @DeleteMapping("/api/admin/directors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除导演", description = "删除导演，需要管理员权限")
    public ResponseResult<Void> deleteDirector(@PathVariable Long id) {
        directorService.deleteDirector(id);
        return ResponseResult.success();
    }

    @GetMapping("/api/directors/popular")
    @Operation(summary = "获取热门导演", description = "获取热门导演列表")
    public ResponseResult<List<DirectorVO>> getPopularDirectors(
            @RequestParam(defaultValue = "5") int limit) {
        
        return ResponseResult.success(directorService.getPopularDirectors(limit));
    }
} 