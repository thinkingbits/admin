package com.jerry.ff.controller;

import com.jerry.ff.model.dto.PlayHistoryDTO;
import com.jerry.ff.model.vo.PlayHistoryVO;
import com.jerry.ff.service.PlayHistoryService;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Tag(name = "播放历史", description = "用户播放历史相关接口")
public class PlayHistoryController {

    private final PlayHistoryService playHistoryService;

    @PostMapping
    @Operation(summary = "记录播放历史", description = "记录用户的电影播放历史")
    public ResponseResult<PlayHistoryVO> addPlayHistory(
            @Valid @RequestBody PlayHistoryDTO playHistoryDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        return ResponseResult.success(playHistoryService.addPlayHistory(userDetails.getUsername(), playHistoryDTO));
    }

    @GetMapping
    @Operation(summary = "获取播放历史", description = "获取当前用户的播放历史")
    public ResponseResult<Page<PlayHistoryVO>> getPlayHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseResult.success(playHistoryService.getPlayHistory(userDetails.getUsername(), pageable));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除播放历史", description = "删除指定的播放历史记录")
    public ResponseResult<Void> deletePlayHistory(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        playHistoryService.deletePlayHistory(id, userDetails.getUsername());
        return ResponseResult.success();
    }

    @DeleteMapping
    @Operation(summary = "清空播放历史", description = "清空当前用户的所有播放历史")
    public ResponseResult<Void> clearPlayHistory(@AuthenticationPrincipal UserDetails userDetails) {
        playHistoryService.clearPlayHistory(userDetails.getUsername());
        return ResponseResult.success();
    }
} 