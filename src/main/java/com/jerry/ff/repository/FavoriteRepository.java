package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Page<Favorite> findByUserId(Long userId, Pageable pageable);
    
    Page<Favorite> findByUserIdAndStatus(Long userId, Integer status, Pageable pageable);
    
    Optional<Favorite> findByUserIdAndEpisodeId(Long userId, Long episodeId);
    
    @Query("SELECT COUNT(f) > 0 FROM Favorite f WHERE f.user.id = :userId AND f.episode.id = :episodeId AND f.status = 1")
    boolean existsByUserIdAndEpisodeIdAndStatusActive(@Param("userId") Long userId, @Param("episodeId") Long episodeId);
}