package com.jerry.ff.service.impl;

import com.jerry.ff.exception.BusinessException;
import com.jerry.ff.model.entity.*;
import com.jerry.ff.model.vo.DramaVO;
import com.jerry.ff.model.vo.EpisodeVO;
import com.jerry.ff.model.vo.SeasonVO;
import com.jerry.ff.repository.*;
import com.jerry.ff.service.DramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DramaServiceImpl implements DramaService {
    private final DramaRepository dramaRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;
    private final UserDramaFollowRepository userDramaFollowRepository;
    private final UserWatchHistoryRepository userWatchHistoryRepository;

    @Override
    public DramaVO getDrama(Long id) {
        Drama drama = dramaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("电视剧不存在"));
        return convertToDramaVO(drama);
    }

    @Override
    public Page<DramaVO> getDramaList(Long categoryId, Pageable pageable) {
        Page<Drama> dramaPage;
        if (categoryId != null) {
            dramaPage = dramaRepository.findByCategoryId(categoryId, pageable);
        } else {
            dramaPage = dramaRepository.findAll(pageable);
        }
        
        List<DramaVO> dramaVOs = dramaPage.getContent().stream()
                .map(this::convertToDramaVO)
                .collect(Collectors.toList());
                
        return new PageImpl<>(dramaVOs, pageable, dramaPage.getTotalElements());
    }

    @Override
    public Page<DramaVO> getActiveDramas(Pageable pageable) {
        Page<Drama> dramaPage = dramaRepository.findByStatus(1, pageable);
        List<DramaVO> dramaVOs = dramaPage.getContent().stream()
                .map(this::convertToDramaVO)
                .collect(Collectors.toList());
                
        return new PageImpl<>(dramaVOs, pageable, dramaPage.getTotalElements());
    }

    @Override
    public List<DramaVO> getLatestDramas(int limit) {
        return dramaRepository.findLatestDramas(1, LocalDate.now(), PageRequest.of(0, limit))
                .stream()
                .map(this::convertToDramaVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DramaVO> getOngoingDramas(int limit) {
        return dramaRepository.findOngoingDramas(PageRequest.of(0, limit))
                .stream()
                .map(this::convertToDramaVO)
                .collect(Collectors.toList());
    }

    @Override
    public SeasonVO getSeason(Long dramaId, Integer seasonNumber) {
//        Season season = seasonRepository.findByDramaIdAndSeasonNumber(dramaId, seasonNumber)
//                .orElseThrow(() -> new BusinessException("季不存在"));
        return convertToSeasonVO(new Season());
    }

    @Override
    public EpisodeVO getEpisode(Long seasonId, Integer episodeNumber) {
        Episode episode = episodeRepository.findBySeasonIdAndEpisodeNumber(seasonId, episodeNumber)
                .orElseThrow(() -> new BusinessException("剧集不存在"));
        return convertToEpisodeVO(episode);
    }

    @Override
    @Transactional
    public void updateWatchProgress(Long userId, Long episodeId, Integer progress) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new BusinessException("剧集不存在"));
                
        UserWatchHistory watchHistory = userWatchHistoryRepository
                .findByUserIdAndEpisodeId(userId, episodeId)
                .orElse(new UserWatchHistory());
                
        watchHistory.setUserId(userId);
        watchHistory.setEpisodeId(episodeId);
        watchHistory.setDramaId(episode.getSeason().getMediaId());
        watchHistory.setProgress(progress);
        
        userWatchHistoryRepository.save(watchHistory);
    }

    @Override
    @Transactional
    public void followDrama(Long userId, Long dramaId) {
        if (!dramaRepository.existsById(dramaId)) {
            throw new BusinessException("电视剧不存在");
        }
        
        if (!userDramaFollowRepository.existsByUserIdAndDramaId(userId, dramaId)) {
            UserDramaFollow follow = new UserDramaFollow();
            follow.setUserId(userId);
            follow.setDramaId(dramaId);
            userDramaFollowRepository.save(follow);
        }
    }

    @Override
    public List<DramaVO> getUserFollowedDramas(Long userId) {
        List<UserDramaFollow> follows = userDramaFollowRepository.findByUserId(userId);
        List<Drama> dramas = dramaRepository.findAllById(
                follows.stream()
                        .map(UserDramaFollow::getDramaId)
                        .collect(Collectors.toList())
        );
        
        return dramas.stream()
                .map(this::convertToDramaVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DramaVO> getUserWatchHistory(Long userId, int limit) {
        List<UserWatchHistory> histories = userWatchHistoryRepository
                .findByUserIdOrderByUpdateTimeDesc(userId, PageRequest.of(0, limit));
                
        List<Drama> dramas = dramaRepository.findAllById(
                histories.stream()
                        .map(UserWatchHistory::getDramaId)
                        .collect(Collectors.toList())
        );
        
        return dramas.stream()
                .map(this::convertToDramaVO)
                .collect(Collectors.toList());
    }

    private DramaVO convertToDramaVO(Drama drama) {
        List<SeasonVO> seasonVOs = new ArrayList<>();
        if (drama.getSeasons() != null) {
            seasonVOs = drama.getSeasons().stream()
                    .map(this::convertToSeasonVO)
                    .collect(Collectors.toList());
        }

        return DramaVO.builder()
                .id(drama.getId())
                .title(drama.getTitle())
                .originalTitle(drama.getOriginalTitle())
                .description(drama.getDescription())
                .posterUrl(drama.getPosterUrl())
                .bannerUrl(drama.getBannerUrl())
                .releaseDate(drama.getReleaseDate())
                .endDate(drama.getEndDate())
                .region(drama.getRegion())
                .language(drama.getLanguage())
                .status(drama.getStatus())
                .createTime(drama.getCreateTime())
                .updateTime(drama.getUpdateTime())
                .seasons(seasonVOs)
                .build();
    }

    private SeasonVO convertToSeasonVO(Season season) {
        List<EpisodeVO> episodeVOs = new ArrayList<>();
        if (season.getEpisodes() != null) {
            episodeVOs = season.getEpisodes().stream()
                    .map(this::convertToEpisodeVO)
                    .collect(Collectors.toList());
        }

        return SeasonVO.builder()
                .id(season.getId())
                .mediaId(season.getMediaId())
                .seasonNumber(season.getSeasonNumber())
                .title(season.getTitle())
                .overview(season.getOverview())
                .posterUrl(season.getPosterUrl())
                .episodes(episodeVOs)
                .build();
    }

    private EpisodeVO convertToEpisodeVO(Episode episode) {
        return EpisodeVO.builder()
                .id(episode.getId())
                .seasonId(episode.getSeason().getId())
                .episodeNumber(episode.getEpisodeNumber())
                .title(episode.getTitle())
                .overview(episode.getOverview())
                .stillUrl(episode.getStillUrl())
                .duration(episode.getDuration())
                .airDate(episode.getAirDate())
                .build();
    }
} 