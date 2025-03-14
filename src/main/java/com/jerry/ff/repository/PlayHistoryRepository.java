package com.jerry.ff.repository;

import com.jerry.ff.model.entity.PlayHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {
    
    Optional<PlayHistory> findByUserIdAndEpisodeId(Long userId, Long episodeId);
    
    Page<PlayHistory> findByUserIdOrderByLastPlayTimeDesc(Long userId, Pageable pageable);
    
    void deleteByUserId(Long userId);
} 