package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Director;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {

    Page<Director> findByNameContaining(String name, Pageable pageable);

    boolean existsByName(String name);

    @Query("SELECT d FROM Director d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(d.originalName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Director> searchDirectors(@Param("keyword") String keyword, Pageable pageable);

    List<Director> findByIdIn(List<Long> ids);

    @Query("SELECT DISTINCT d FROM Director d JOIN d.films f WHERE f.id = :filmId")
    List<Director> findByFilmId(@Param("filmId") Long filmId);

    Page<Director> findByNameContainingOrOriginalNameContaining(String name, String originalName, Pageable pageable);
} 