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

    @Query(value = "SELECT d.* FROM directors d " +
            "JOIN movie_directors md ON d.id = md.director_id " +
            "GROUP BY d.id " +
            "ORDER BY COUNT(md.movie_id) DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Director> findPopularDirectors(@Param("limit") int limit);
} 