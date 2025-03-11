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

    private final FilmService movieService;

    @GetMapping("/api/movies")
    @Operation(summary = "获取电影列表", description = "分页获取电影列表")
    public ResponseResult<Page<FilmVO>> getMovies(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseResult.success(movieService.getMovies(categoryId, pageable));
    }

    @GetMapping("/api/movies/{id}")
    @Operation(summary = "获取电影详情", description = "根据ID获取电影详情")
    public ResponseResult<FilmVO> getMovie(@PathVariable Long id) {
        return ResponseResult.success(movieService.getMovie(id));
    }

    @PostMapping("/api/admin/movies")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "创建电影", description = "创建新电影，需要管理员权限")
    public ResponseResult<FilmVO> createMovie(@Valid @RequestBody FilmDTO movieDTO) {
        return ResponseResult.success(movieService.createMovie(movieDTO));
    }

    @PutMapping("/api/admin/movies/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新电影", description = "更新电影信息，需要管理员权限")
    public ResponseResult<FilmVO> updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody FilmDTO movieDTO) {
        
        return ResponseResult.success(movieService.updateMovie(id, movieDTO));
    }

    @DeleteMapping("/api/admin/movies/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除电影", description = "删除电影，需要管理员权限")
    public ResponseResult<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseResult.success();
    }

    @PutMapping("/api/admin/movies/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新电影状态", description = "更新电影状态（上架/下架），需要管理员权限")
    public ResponseResult<FilmVO> updateMovieStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        
        return ResponseResult.success(movieService.updateMovieStatus(id, status));
    }

    @GetMapping("/api/movies/featured")
    @Operation(summary = "获取推荐电影", description = "获取推荐电影列表")
    public ResponseResult<List<FilmVO>> getFeaturedMovies(
            @RequestParam(defaultValue = "6") int limit) {
        
        return ResponseResult.success(movieService.getFeaturedMovies(limit));
    }

    @GetMapping("/api/movies/latest")
    @Operation(summary = "获取最新电影", description = "获取最新上架电影列表")
    public ResponseResult<List<FilmVO>> getLatestMovies(
            @RequestParam(defaultValue = "6") int limit) {
        
        return ResponseResult.success(movieService.getLatestMovies(limit));
    }

    @GetMapping("/api/movies/popular")
    @Operation(summary = "获取热门电影", description = "获取评分最高电影列表")
    public ResponseResult<List<FilmVO>> getPopularMovies(
            @RequestParam(defaultValue = "6") int limit) {
        
        return ResponseResult.success(movieService.getPopularMovies(limit));
    }
} 