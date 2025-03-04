package com.jerry.ff.repository;

import com.jerry.ff.model.entity.MovieSeries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieSeriesRepository extends JpaRepository<MovieSeries, Long> {
    Page<MovieSeries> findByStatus(Integer status, Pageable pageable);
    
    Page<MovieSeries> findByCategoryId(Long categoryId, Pageable pageable);
    
    Page<MovieSeries> findByTitleContainingAndStatus(String keyword, Integer status, Pageable pageable);
    
    List<MovieSeries> findByStatusOrderByRatingDesc(Integer status, Pageable pageable);
    
    @Query("SELECT ms FROM MovieSeries ms WHERE ms.status = 1 ORDER BY ms.createTime DESC")
    List<MovieSeries> findLatestMovieSeries(Pageable pageable);
} 