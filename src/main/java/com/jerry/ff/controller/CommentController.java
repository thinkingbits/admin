package com.jerry.ff.controller;

import com.jerry.ff.model.dto.CommentDTO;
import com.jerry.ff.model.vo.CommentVO;
import com.jerry.ff.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentVO>> getAllComments(Pageable pageable) {
        return ResponseEntity.ok(commentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentVO> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @GetMapping("/episode/{episodeId}")
    public ResponseEntity<Page<CommentVO>> getCommentsByEpisode(@PathVariable Long episodeId, Pageable pageable) {
        return ResponseEntity.ok(commentService.findByEpisodeId(episodeId, pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CommentVO>> getCommentsByUser(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(commentService.findByUserId(userId, pageable));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CommentVO>> getCommentsByStatus(@PathVariable Integer status, Pageable pageable) {
        return ResponseEntity.ok(commentService.findByStatus(status, pageable));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CommentVO>> getPendingComments(Pageable pageable) {
        return ResponseEntity.ok(commentService.getPendingComments(pageable));
    }

    @PostMapping("/film/{filmId}")
    public ResponseEntity<CommentVO> addComment(
            @PathVariable Long filmId,
            @RequestBody CommentDTO commentDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(commentService.addComment(filmId, commentDTO, userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<CommentVO> createComment(@RequestBody CommentVO commentVO) {
        return ResponseEntity.ok(commentService.create(commentVO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentVO> updateComment(
            @PathVariable Long id,
            @RequestBody CommentVO commentVO,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (commentService.isCommentOwner(id, userDetails.getUsername())) {
            return ResponseEntity.ok(commentService.update(id, commentVO));
        }
        return ResponseEntity.status(403).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (commentService.isCommentOwner(id, userDetails.getUsername())) {
            commentService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommentVO> approveComment(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.approve(id));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommentVO> rejectComment(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.reject(id));
    }

    @PutMapping("/{id}/audit/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommentVO> auditComment(@PathVariable Long id, @PathVariable Integer status) {
        return ResponseEntity.ok(commentService.auditComment(id, status));
    }
} 