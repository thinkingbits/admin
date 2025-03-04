package com.jerry.ff.repository;

import com.jerry.ff.model.entity.UserWatchHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserWatchHistoryRepository extends JpaRepository<UserWatchHistory, Long> {
    Optional<UserWatchHistory> findByUserIdAndEpisodeId(Long userId, Long episodeId);
    List<UserWatchHistory> findByUserIdOrderByUpdateTimeDesc(Long userId, Pageable pageable);
} 