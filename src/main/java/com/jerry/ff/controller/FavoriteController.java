package com.jerry.ff.controller;

import com.jerry.ff.model.vo.MovieVO;
import com.jerry.ff.service.FavoriteService;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Tag(name = "收藏管理", description = "用户收藏相关接口")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/movies/{movieId}")
    @Operation(summary = "收藏电影", description = "收藏指定电影")
    public ResponseResult<Boolean> addFavorite(
            @PathVariable Long movieId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        favoriteService.addFavorite(userDetails.getUsername(), movieId);
        return ResponseResult.success(true);
    }

    @DeleteMapping("/movies/{movieId}")
    @Operation(summary = "取消收藏", description = "取消收藏指定电影")
    public ResponseResult<Boolean> removeFavorite(
            @PathVariable Long movieId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        favoriteService.removeFavorite(userDetails.getUsername(), movieId);
        return ResponseResult.success(true);
    }

    @GetMapping("/movies")
    @Operation(summary = "获取收藏的电影", description = "获取当前用户收藏的所有电影")
    public ResponseResult<Page<MovieVO>> getFavoriteMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseResult.success(favoriteService.getFavoriteMovies(userDetails.getUsername(), pageable));
    }

    @GetMapping("/check/{movieId}")
    @Operation(summary = "检查收藏状态", description = "检查当前用户是否已收藏指定电影")
    public ResponseResult<Boolean> checkFavorite(
            @PathVariable Long movieId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        return ResponseResult.success(favoriteService.checkFavorite(userDetails.getUsername(), movieId));
    }
} 