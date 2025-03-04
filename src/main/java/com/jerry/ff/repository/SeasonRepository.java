package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findByTvSeriesIdOrderBySeasonNumber(Long tvSeriesId);
    
    Optional<Season> findByTvSeriesIdAndSeasonNumber(Long tvSeriesId, Integer seasonNumber);

    Optional<Season> findByDramaIdAndSeasonNumber(Long dramaId, Integer seasonNumber);
} 