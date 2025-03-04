package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Drama;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DramaRepository extends JpaRepository<Drama, Long> {
    Page<Drama> findByStatus(Integer status, Pageable pageable);
    
    Page<Drama> findByCategoryId(Long categoryId, Pageable pageable);
    
    Page<Drama> findByTitleContainingAndStatus(String keyword, Integer status, Pageable pageable);
    
    List<Drama> findByStatusOrderByRatingDesc(Integer status, Pageable pageable);
    
    @Query("SELECT d FROM Drama d WHERE d.status = :status AND d.releaseDate <= :now ORDER BY d.releaseDate DESC")
    List<Drama> findLatestDramas(@Param("status") Integer status, @Param("now") LocalDate now, Pageable pageable);
    
    @Query("SELECT d FROM Drama d WHERE d.status = 2")
    List<Drama> findOngoingDramas(Pageable pageable);
} 