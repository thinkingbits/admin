package com.jerry.ff.service;

import com.jerry.ff.model.dto.CommentDTO;
import com.jerry.ff.model.vo.CommentVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    
    Page<CommentVO> getCommentsByMovie(Long movieId, Pageable pageable);
    
    CommentVO addComment(Long movieId, CommentDTO commentDTO, String username);
    
    void deleteComment(Long id);
    
    CommentVO auditComment(Long id, Integer status);
    
    Page<CommentVO> getPendingComments(Pageable pageable);
    
    boolean isCommentOwner(Long id, String username);

    CommentVO create(CommentVO commentVO);
    CommentVO update(Long id, CommentVO commentVO);
    void delete(Long id);
    CommentVO findById(Long id);
    Page<CommentVO> findAll(Pageable pageable);
    Page<CommentVO> findByEpisodeId(Long episodeId, Pageable pageable);
    Page<CommentVO> findByUserId(Long userId, Pageable pageable);
    Page<CommentVO> findByStatus(Integer status, Pageable pageable);
    CommentVO approve(Long id);
    CommentVO reject(Long id);
} 