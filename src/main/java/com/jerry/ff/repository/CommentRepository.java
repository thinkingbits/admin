package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByEpisodeId(Long episodeId, Pageable pageable);
    Page<Comment> findByUserId(Long userId, Pageable pageable);
    Page<Comment> findByStatus(Integer status, Pageable pageable);
} 