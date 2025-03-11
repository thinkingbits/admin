package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Film, Long> {

    Page<Film> findByStatus(Integer status, Pageable pageable);

    Page<Film> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Film> findByCategoryIdAndStatus(Long categoryId, Integer status, Pageable pageable);

    @Query("SELECT m FROM Film m WHERE m.title LIKE %:keyword% OR m.description LIKE %:keyword%")
    Page<Film> searchMovies(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT m FROM Film m WHERE m.status = 1 ORDER BY m.rating DESC")
    List<Film> findTopRatedMovies(Pageable pageable);

    @Query("SELECT m FROM Film m WHERE m.status = 1 ORDER BY m.createTime DESC")
    List<Film> findLatestMovies(Pageable pageable);

    List<Film> findByStatusOrderByCreateTimeDesc(Integer status, Pageable pageable);
    
    List<Film> findByStatusOrderByReleaseDateDesc(Integer status, Pageable pageable);
    
    List<Film> findByStatusOrderByRatingDesc(Integer status, Pageable pageable);
    
    Page<Film> findByTitleContainingAndStatus(String keyword, Integer status, Pageable pageable);
    
//    boolean existsByDirectorsId(Long directorId);
} 