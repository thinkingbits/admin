package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Anime;
import com.jerry.ff.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Anime, Long> {

//    Page<Comment> findByMovieId(Long movieId, Pageable pageable);
//
//    Page<Comment> findByUserId(Long userId, Pageable pageable);
//
//    Page<Comment> findByMovieIdAndStatus(Long movieId, Integer status, Pageable pageable);
//
//    long countByStatus(Integer status);
//
//    List<Comment> findByMovieIdAndStatus(Long movieId, Integer status, Sort sort);
//
//    Page<Comment> findByStatus(Integer status, Pageable pageable);
} 