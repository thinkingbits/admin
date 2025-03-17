package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findBySeasonIdOrderByEpisodeNumber(Long seasonId);
    
    Optional<Episode> findBySeasonIdAndEpisodeNumber(Long seasonId, Integer episodeNumber);
    
    @Query("UPDATE Episode e SET e.viewCount = e.viewCount + 1 WHERE e.id = :episodeId")
    @Modifying
    void incrementViewCount(@Param("episodeId") Long episodeId);

    List<Episode> findByFilmId(Long filmId);
    Optional<Episode> findFirstByFilmIdOrderByEpisodeNumberAsc(Long filmId);
} 