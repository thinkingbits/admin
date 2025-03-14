package com.jerry.ff.controller;

import com.jerry.ff.model.dto.FilmDTO;
import com.jerry.ff.model.vo.FilmVO;
import com.jerry.ff.service.FilmService;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "电影管理", description = "电影相关接口")
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/api/films")
    @Operation(summary = "获取电影列表", description = "分页获取电影列表")
    public ResponseResult<Page<FilmVO>> getFilms(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseResult.success(filmService.getFilms(categoryId, pageable));
    }

    @GetMapping("/api/films/{id}")
    @Operation(summary = "获取电影详情", description = "根据ID获取电影详情")
    public ResponseResult<FilmVO> getFilm(@PathVariable Long id) {
        return ResponseResult.success(filmService.getFilm(id));
    }

    @PostMapping("/api/admin/films")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "创建电影", description = "创建新电影，需要管理员权限")
    public ResponseResult<FilmVO> createFilm(@Valid @RequestBody FilmDTO filmDTO) {
        return ResponseResult.success(filmService.createFilm(filmDTO));
    }

    @PutMapping("/api/admin/films/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新电影", description = "更新电影信息，需要管理员权限")
    public ResponseResult<FilmVO> updateFilm(
            @PathVariable Long id,
            @Valid @RequestBody FilmDTO filmDTO) {
        
        return ResponseResult.success(filmService.updateFilm(id, filmDTO));
    }

    @DeleteMapping("/api/admin/films/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除电影", description = "删除电影，需要管理员权限")
    public ResponseResult<Void> deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
        return ResponseResult.success();
    }

    @PutMapping("/api/admin/films/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新电影状态", description = "更新电影状态（上架/下架），需要管理员权限")
    public ResponseResult<FilmVO> updateFilmStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        
        return ResponseResult.success(filmService.updateFilmStatus(id, status));
    }

    @GetMapping("/api/films/featured")
    @Operation(summary = "获取推荐电影", description = "获取推荐电影列表")
    public ResponseResult<List<FilmVO>> getFeaturedFilms(
            @RequestParam(defaultValue = "6") int limit) {
        
        return ResponseResult.success(filmService.getFeaturedFilms(limit));
    }

    @GetMapping("/api/films/latest")
    @Operation(summary = "获取最新电影", description = "获取最新上架电影列表")
    public ResponseResult<List<FilmVO>> getLatestFilms(
            @RequestParam(defaultValue = "6") int limit) {
        
        return ResponseResult.success(filmService.getLatestFilms(limit));
    }

    @GetMapping("/api/films/popular")
    @Operation(summary = "获取热门电影", description = "获取评分最高电影列表")
    public ResponseResult<List<FilmVO>> getPopularFilms(
            @RequestParam(defaultValue = "6") int limit) {
        
        return ResponseResult.success(filmService.getPopularFilms(limit));
    }
} 