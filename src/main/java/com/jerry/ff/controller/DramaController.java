package com.jerry.ff.controller;

import com.jerry.ff.model.vo.DramaVO;
import com.jerry.ff.model.vo.EpisodeVO;
import com.jerry.ff.model.vo.SeasonVO;
import com.jerry.ff.service.DramaService;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dramas")
@Tag(name = "电视剧管理", description = "电视剧相关接口")
@RequiredArgsConstructor
public class DramaController {
    private final DramaService dramaService;
    
    @GetMapping
    @Operation(summary = "获取电视剧列表")
    public ResponseResult<Page<DramaVO>> getDramaList(
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "createTime") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseResult.success(dramaService.getDramaList(categoryId, pageable));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取电视剧详情")
    public ResponseResult<DramaVO> getDrama(@PathVariable Long id) {
        return ResponseResult.success(dramaService.getDrama(id));
    }
    
    @GetMapping("/latest")
    @Operation(summary = "获取最新电视剧")
    public ResponseResult<List<DramaVO>> getLatestDramas(
            @Parameter(description = "获取数量") @RequestParam(defaultValue = "10") int limit) {
        return ResponseResult.success(dramaService.getLatestDramas(limit));
    }
    
    @GetMapping("/ongoing")
    @Operation(summary = "获取连载中的电视剧")
    public ResponseResult<List<DramaVO>> getOngoingDramas(
            @Parameter(description = "获取数量") @RequestParam(defaultValue = "10") int limit) {
        return ResponseResult.success(dramaService.getOngoingDramas(limit));
    }
    
    @GetMapping("/{id}/seasons/{seasonNumber}")
    @Operation(summary = "获取季详情")
    public ResponseResult<SeasonVO> getSeason(
            @PathVariable Long id,
            @PathVariable Integer seasonNumber) {
        return ResponseResult.success(dramaService.getSeason(id, seasonNumber));
    }
    
    @GetMapping("/seasons/{seasonId}/episodes/{episodeNumber}")
    @Operation(summary = "获取剧集详情")
    public ResponseResult<EpisodeVO> getEpisode(
            @PathVariable Long seasonId,
            @PathVariable Integer episodeNumber) {
        return ResponseResult.success(dramaService.getEpisode(seasonId, episodeNumber));
    }
    
    @PostMapping("/{id}/follow")
    @Operation(summary = "追剧")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult<Void> followDrama(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        dramaService.followDrama(
                Long.parseLong(userDetails.getUsername()), 
                id);
        return ResponseResult.success();
    }
    
    @PostMapping("/episodes/{episodeId}/progress")
    @Operation(summary = "更新观看进度")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult<Void> updateWatchProgress(
            @PathVariable Long episodeId,
            @RequestParam Integer progress,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        dramaService.updateWatchProgress(
                Long.parseLong(userDetails.getUsername()),
                episodeId,
                progress);
        return ResponseResult.success();
    }
    
    @GetMapping("/followed")
    @Operation(summary = "获取追剧列表")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult<List<DramaVO>> getFollowedDramas(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        return ResponseResult.success(
                dramaService.getUserFollowedDramas(
                        Long.parseLong(userDetails.getUsername())));
    }
    
    @GetMapping("/history")
    @Operation(summary = "获取观看历史")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult<List<DramaVO>> getWatchHistory(
            @Parameter(description = "获取数量") @RequestParam(defaultValue = "50") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        return ResponseResult.success(
                dramaService.getUserWatchHistory(
                        Long.parseLong(userDetails.getUsername()),
                        limit));
    }
} 