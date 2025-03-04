package com.jerry.ff.controller;

import com.jerry.ff.model.dto.CategoryDTO;
import com.jerry.ff.model.vo.CategoryVO;
import com.jerry.ff.service.CategoryService;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "分类管理", description = "电影分类相关接口")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取所有分类", description = "获取所有电影分类")
    public ResponseResult<List<CategoryVO>> getAllCategories() {
        return ResponseResult.success(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情", description = "获取指定ID的分类详情")
    public ResponseResult<CategoryVO> getCategory(@PathVariable Long id) {
        return ResponseResult.success(categoryService.getCategory(id));
    }

    @GetMapping("/parent")
    @Operation(summary = "获取父分类", description = "获取所有父级分类")
    public ResponseResult<List<CategoryVO>> getParentCategories() {
        return ResponseResult.success(categoryService.getParentCategories());
    }

    @GetMapping("/parent/{parentId}")
    @Operation(summary = "获取子分类", description = "获取指定父级ID的所有子分类")
    public ResponseResult<List<CategoryVO>> getChildCategories(@PathVariable Long parentId) {
        return ResponseResult.success(categoryService.getChildCategories(parentId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "创建分类", description = "创建新分类，需要管理员权限")
    public ResponseResult<CategoryVO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseResult.success(categoryService.createCategory(categoryDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新分类", description = "更新指定ID的分类，需要管理员权限")
    public ResponseResult<CategoryVO> updateCategory(
            @PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseResult.success(categoryService.updateCategory(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除分类", description = "删除指定ID的分类，需要管理员权限")
    public ResponseResult<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseResult.success();
    }
} 