package com.jerry.ff.controller;

import com.jerry.ff.model.dto.CommentDTO;
import com.jerry.ff.model.vo.CommentVO;
import com.jerry.ff.service.CommentService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "评论管理", description = "电影评论相关接口")
public class CommentController {

    private final CommentService commentService;

//    @GetMapping("/api/movies/{movieId}/comments")
//    @Operation(summary = "获取电影评论", description = "获取指定电影的评论")
//    public ResponseResult<Page<CommentVO>> getMovieComments(
//            @PathVariable Long movieId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createTime") String sortBy,
//            @RequestParam(defaultValue = "DESC") String direction) {
//
//        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
//        Pageable pageable = PageRequest.of(page, size, sort);
//        return ResponseResult.success(commentService.getCommentsByMovie(movieId, pageable));
//    }
//
//    @PostMapping("/api/movies/{movieId}/comments")
//    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("isAuthenticated()")
//    @Operation(summary = "添加评论", description = "为指定电影添加评论")
//    public ResponseResult<CommentVO> addComment(
//            @PathVariable Long movieId,
//            @Valid @RequestBody CommentDTO commentDTO,
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        return ResponseResult.success(commentService.addComment(movieId, commentDTO, userDetails.getUsername()));
//    }
//
//    @DeleteMapping("/api/comments/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasRole('ADMIN') or @commentService.isCommentOwner(#id, authentication.name)")
//    @Operation(summary = "删除评论", description = "删除指定评论，需要管理员权限或是评论作者")
//    public ResponseResult<Void> deleteComment(@PathVariable Long id) {
//        commentService.deleteComment(id);
//        return ResponseResult.success();
//    }
//
//    @PutMapping("/api/comments/{id}/audit")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "审核评论", description = "审核指定评论，需要管理员权限")
//    public ResponseResult<CommentVO> auditComment(
//            @PathVariable Long id,
//            @RequestParam Integer status) {
//
//        return ResponseResult.success(commentService.auditComment(id, status));
//    }
//
//    @GetMapping("/api/admin/comments/pending")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "获取待审核评论", description = "获取所有待审核评论，需要管理员权限")
//    public ResponseResult<Page<CommentVO>> getPendingComments(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseResult.success(commentService.getPendingComments(pageable));
//    }
} 