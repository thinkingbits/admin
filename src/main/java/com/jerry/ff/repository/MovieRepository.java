package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findByStatus(Integer status, Pageable pageable);

    Page<Movie> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Movie> findByCategoryIdAndStatus(Long categoryId, Integer status, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.title LIKE %:keyword% OR m.description LIKE %:keyword%")
    Page<Movie> searchMovies(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.status = 1 ORDER BY m.rating DESC")
    List<Movie> findTopRatedMovies(Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.status = 1 ORDER BY m.createTime DESC")
    List<Movie> findLatestMovies(Pageable pageable);

    List<Movie> findByStatusOrderByCreateTimeDesc(Integer status, Pageable pageable);
    
    List<Movie> findByStatusOrderByReleaseDateDesc(Integer status, Pageable pageable);
    
    List<Movie> findByStatusOrderByRatingDesc(Integer status, Pageable pageable);
    
    Page<Movie> findByTitleContainingAndStatus(String keyword, Integer status, Pageable pageable);
    
    boolean existsByDirectorsId(Long directorId);
} 