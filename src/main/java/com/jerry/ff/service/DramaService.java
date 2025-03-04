package com.jerry.ff.service;

import com.jerry.ff.model.vo.DramaVO;
import com.jerry.ff.model.vo.EpisodeVO;
import com.jerry.ff.model.vo.SeasonVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DramaService {
    DramaVO getDrama(Long id);
    
    Page<DramaVO> getDramaList(Long categoryId, Pageable pageable);
    
    Page<DramaVO> getActiveDramas(Pageable pageable);
    
    List<DramaVO> getLatestDramas(int limit);
    
    List<DramaVO> getOngoingDramas(int limit);
    
    SeasonVO getSeason(Long dramaId, Integer seasonNumber);
    
    EpisodeVO getEpisode(Long seasonId, Integer episodeNumber);
    
    void updateWatchProgress(Long userId, Long episodeId, Integer progress);
    
    void followDrama(Long userId, Long dramaId);
    
    List<DramaVO> getUserFollowedDramas(Long userId);
    
    List<DramaVO> getUserWatchHistory(Long userId, int limit);
} 