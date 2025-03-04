package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Anime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    Page<Anime> findByStatus(Integer status, Pageable pageable);
    
    Page<Anime> findByCategoryId(Long categoryId, Pageable pageable);
    
    Page<Anime> findByTitleContainingAndStatus(String keyword, Integer status, Pageable pageable);
    
    List<Anime> findByStatusOrderByRatingDesc(Integer status, Pageable pageable);
    
    @Query("SELECT a FROM Anime a WHERE a.status = :status AND a.releaseDate <= :now ORDER BY a.releaseDate DESC")
    List<Anime> findLatestAnimes(Integer status, LocalDate now, Pageable pageable);
    
    @Query("SELECT a FROM Anime a WHERE a.status = 2")  // status = 2 表示连载中
    List<Anime> findOngoingAnimes(Pageable pageable);
    
    List<Anime> findByTypeAndStatusOrderByReleaseDateDesc(Anime.AnimeType type, Integer status, Pageable pageable);
    
    @Query("SELECT DISTINCT a.season FROM Anime a WHERE a.status = 1 ORDER BY a.season DESC")
    List<String> findAllSeasons(Pageable pageable);
    
    List<Anime> findBySeasonAndStatusOrderByReleaseDate(String season, Integer status, Pageable pageable);
} 