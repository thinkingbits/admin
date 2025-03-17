package com.jerry.ff.controller;

import com.jerry.ff.model.vo.FavoriteVO;
import com.jerry.ff.service.FavoriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Tag(name = "收藏管理", description = "用户收藏相关接口")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<Page<FavoriteVO>> getUserFavorites(
            Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(favoriteService.getUserFavorites(userDetails.getUsername(), pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<FavoriteVO>> getUserFavorites(
            @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(favoriteService.getUserFavorites(userId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteVO> getFavoriteById(@PathVariable Long id) {
        return ResponseEntity.ok(favoriteService.findById(id));
    }

    @PostMapping("/episode/{episodeId}")
    public ResponseEntity<FavoriteVO> addFavorite(
            @PathVariable Long episodeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(favoriteService.addFavorite(episodeId, userDetails.getUsername()));
    }

    @DeleteMapping("/episode/{episodeId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long episodeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        favoriteService.removeFavorite(episodeId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/{episodeId}")
    public ResponseEntity<Map<String, Boolean>> checkFavorite(
            @PathVariable Long episodeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        boolean isFavorite = favoriteService.isFavorite(episodeId, userDetails.getUsername());
        Map<String, Boolean> result = new HashMap<>();
        result.put("favorite", isFavorite);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FavoriteVO> createFavorite(@RequestBody FavoriteVO favoriteVO) {
        return ResponseEntity.ok(favoriteService.create(favoriteVO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FavoriteVO> updateFavorite(
            @PathVariable Long id,
            @RequestBody FavoriteVO favoriteVO) {
        return ResponseEntity.ok(favoriteService.update(id, favoriteVO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.delete(id);
        return ResponseEntity.ok().build();
    }
} 