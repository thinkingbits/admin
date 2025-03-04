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
} 