package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    
    Page<Actor> findByNameContainingOrOriginalNameContaining(String name, String originalName, Pageable pageable);
    
    @Query("SELECT a FROM Actor a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.originalName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Actor> searchActors(@Param("keyword") String keyword, Pageable pageable);
    
    List<Actor> findByIdIn(List<Long> ids);
    
    boolean existsByName(String name);
    
    @Query("SELECT DISTINCT a FROM Actor a JOIN a.films f WHERE f.id = :filmId")
    List<Actor> findByFilmId(@Param("filmId") Long filmId);
} 