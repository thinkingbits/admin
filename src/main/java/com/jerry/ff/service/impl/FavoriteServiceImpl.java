package com.jerry.ff.service.impl;

import com.jerry.ff.model.entity.Episode;
import com.jerry.ff.model.entity.Favorite;
import com.jerry.ff.model.entity.User;
import com.jerry.ff.model.vo.FavoriteVO;
import com.jerry.ff.repository.EpisodeRepository;
import com.jerry.ff.repository.FavoriteRepository;
import com.jerry.ff.repository.UserRepository;
import com.jerry.ff.service.FavoriteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;

    @Override
    @Transactional
    public FavoriteVO addFavorite(Long episodeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException("Episode not found with id: " + episodeId));
        
        // 检查是否已收藏
        Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndEpisodeId(user.getId(), episodeId);
        
        Favorite favorite;
        if (existingFavorite.isPresent()) {
            // 如果已存在但状态为0，则更新状态为1
            favorite = existingFavorite.get();
            if (favorite.getStatus() == 0) {
                favorite.setStatus(1);
                favorite = favoriteRepository.save(favorite);
            }
        } else {
            // 不存在则创建新收藏
            favorite = Favorite.builder()
                    .user(user)
                    .episode(episode)
                    .status(1)
                    .build();
            favorite = favoriteRepository.save(favorite);
        }
        
        return mapToVO(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(Long episodeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        
        Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndEpisodeId(user.getId(), episodeId);
        
        if (existingFavorite.isPresent()) {
            Favorite favorite = existingFavorite.get();
            favorite.setStatus(0); // 设置为无效
            favoriteRepository.save(favorite);
        }
    }

    @Override
    public boolean isFavorite(Long episodeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        
        return favoriteRepository.existsByUserIdAndEpisodeIdAndStatusActive(user.getId(), episodeId);
    }

    @Override
    public Page<FavoriteVO> getUserFavorites(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        
        return getUserFavorites(user.getId(), pageable);
    }

    @Override
    public Page<FavoriteVO> getUserFavorites(Long userId, Pageable pageable) {
        return favoriteRepository.findByUserIdAndStatus(userId, 1, pageable)
                .map(this::mapToVO);
    }

    @Override
    public FavoriteVO findById(Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found with id: " + id));
        
        return mapToVO(favorite);
    }

    @Override
    @Transactional
    public FavoriteVO create(FavoriteVO favoriteVO) {
        User user = userRepository.findById(favoriteVO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + favoriteVO.getUserId()));
        
        Episode episode = episodeRepository.findById(favoriteVO.getEpisodeId())
                .orElseThrow(() -> new EntityNotFoundException("Episode not found with id: " + favoriteVO.getEpisodeId()));
        
        Favorite favorite = Favorite.builder()
                .user(user)
                .episode(episode)
                .status(favoriteVO.getStatus())
                .build();
        
        favorite = favoriteRepository.save(favorite);
        
        return mapToVO(favorite);
    }

    @Override
    @Transactional
    public FavoriteVO update(Long id, FavoriteVO favoriteVO) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found with id: " + id));
        
        favorite.setStatus(favoriteVO.getStatus());
        favorite = favoriteRepository.save(favorite);
        
        return mapToVO(favorite);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!favoriteRepository.existsById(id)) {
            throw new EntityNotFoundException("Favorite not found with id: " + id);
        }
        favoriteRepository.deleteById(id);
    }
    
    private FavoriteVO mapToVO(Favorite favorite) {
        FavoriteVO vo = new FavoriteVO();
        BeanUtils.copyProperties(favorite, vo);
        
        vo.setUserId(favorite.getUser().getId());
        vo.setUserName(favorite.getUser().getUsername());
        
        vo.setEpisodeId(favorite.getEpisode().getId());
        vo.setEpisodeTitle(favorite.getEpisode().getTitle());
        
        return vo;
    }
} 