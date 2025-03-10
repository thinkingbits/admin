package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Favorite;
import com.jerry.ff.model.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
    
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.user.id = :userId AND f.movie.id = :movieId")
    void deleteByUserIdAndMovieId(@Param("userId") Long userId, @Param("movieId") Long movieId);
    
    @Query("SELECT f.movie FROM Favorite f WHERE f.user.id = :userId AND f.status = 1")
    Page<Movie> findMoviesByUserId(@Param("userId") Long userId, Pageable pageable);
    
    long countByUserId(Long userId);
    
    long countByMovieId(Long movieId);
    
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.user.id = :userId AND f.status = 1")
    long countActiveByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.movie.id = :movieId AND f.status = 1")
    long countActiveByMovieId(@Param("movieId") Long movieId);
} 