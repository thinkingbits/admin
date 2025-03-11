package com.jerry.ff.controller;

import com.jerry.ff.model.vo.FilmVO;
import com.jerry.ff.service.FilmService;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "搜索管理", description = "搜索相关接口")
public class SearchController {

    private final FilmService movieService;

    @GetMapping("/movies")
    @Operation(summary = "搜索电影", description = "根据关键字搜索电影")
    public ResponseResult<Page<FilmVO>> searchMovies(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseResult.error(400, "搜索关键字不能为空");
        }
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseResult.success(movieService.searchMovies(keyword, pageable));
    }
} 