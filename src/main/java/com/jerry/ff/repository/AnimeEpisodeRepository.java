package com.jerry.ff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimeEpisodeRepository extends JpaRepository<AnimeEpisode, Long> {
    List<AnimeEpisode> findByAnimeIdOrderByEpisodeNumber(Long animeId);
    
    Optional<AnimeEpisode> findByAnimeIdAndEpisodeNumber(Long animeId, Integer episodeNumber);
    
    @Modifying
    @Query("UPDATE AnimeEpisode e SET e.viewCount = e.viewCount + 1 WHERE e.id = :episodeId")
    void incrementViewCount(@Param("episodeId") Long episodeId);
} 