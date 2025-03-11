package com.jerry.ff.controller;

import com.jerry.ff.model.vo.FilmSeriesVO;
import com.jerry.ff.service.FilmSeriesService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie-series")
@Tag(name = "电影系列管理", description = "电影系列相关接口")
@RequiredArgsConstructor
public class MovieSeriesController {

    private final FilmSeriesService movieSeriesService;

    @GetMapping("/{id}")
    @Operation(summary = "获取电影系列详情")
    public ResponseResult<FilmSeriesVO> getMovieSeries(@PathVariable Long id) {
        return ResponseResult.success(movieSeriesService.getMovieSeries(id));
    }

    @GetMapping
    @Operation(summary = "获取电影系列列表")
    public ResponseResult<Page<FilmSeriesVO>> getMovieSeriesList(
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "createTime") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "DESC") String direction) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseResult.success(movieSeriesService.getMovieSeriesList(categoryId, pageable));
    }

    @GetMapping("/latest")
    @Operation(summary = "获取最新电影系列")
    public ResponseResult<List<FilmSeriesVO>> getLatestMovieSeries(
            @Parameter(description = "获取数量") @RequestParam(defaultValue = "10") int limit) {
        return ResponseResult.success(movieSeriesService.getLatestMovieSeries(limit));
    }

    @GetMapping("/top-rated")
    @Operation(summary = "获取评分最高的电影系列")
    public ResponseResult<List<FilmSeriesVO>> getTopRatedMovieSeries(
            @Parameter(description = "获取数量") @RequestParam(defaultValue = "10") int limit) {
        return ResponseResult.success(movieSeriesService.getTopRatedMovieSeries(limit));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索电影系列")
    public ResponseResult<Page<FilmSeriesVO>> searchMovieSeries(
            @Parameter(description = "关键词") @RequestParam String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseResult.success(movieSeriesService.searchMovieSeries(keyword, pageable));
    }

    @PostMapping
    @Operation(summary = "创建电影系列")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult<FilmSeriesVO> createMovieSeries(@RequestBody FilmSeriesVO movieSeriesVO) {
        return ResponseResult.success(movieSeriesService.createMovieSeries(movieSeriesVO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新电影系列")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult<FilmSeriesVO> updateMovieSeries(
            @PathVariable Long id,
            @RequestBody FilmSeriesVO movieSeriesVO) {
        return ResponseResult.success(movieSeriesService.updateMovieSeries(id, movieSeriesVO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除电影系列")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult<Void> deleteMovieSeries(@PathVariable Long id) {
        movieSeriesService.deleteMovieSeries(id);
        return ResponseResult.success();
    }

    @PostMapping("/{seriesId}/movies/{movieId}")
    @Operation(summary = "添加电影到系列")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult<Void> addMovieToSeries(
            @PathVariable Long seriesId,
            @PathVariable Long movieId,
            @Parameter(description = "在系列中的顺序") @RequestParam Integer seriesOrder) {
        movieSeriesService.addMovieToSeries(seriesId, movieId, seriesOrder);
        return ResponseResult.success();
    }

    @DeleteMapping("/{seriesId}/movies/{movieId}")
    @Operation(summary = "从系列中移除电影")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult<Void> removeMovieFromSeries(
            @PathVariable Long seriesId,
            @PathVariable Long movieId) {
        movieSeriesService.removeMovieFromSeries(seriesId, movieId);
        return ResponseResult.success();
    }

    @PutMapping("/{seriesId}/movies/{movieId}/order")
    @Operation(summary = "更新电影在系列中的顺序")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult<Void> updateMovieSeriesOrder(
            @PathVariable Long seriesId,
            @PathVariable Long movieId,
            @Parameter(description = "新的顺序") @RequestParam Integer newOrder) {
        movieSeriesService.updateMovieSeriesOrder(seriesId, movieId, newOrder);
        return ResponseResult.success();
    }
} 