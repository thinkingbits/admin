package com.jerry.ff.service;

import com.jerry.ff.model.vo.FavoriteVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteService {
    /**
     * 添加收藏
     */
    FavoriteVO addFavorite(Long episodeId, String username);
    
    /**
     * 取消收藏
     */
    void removeFavorite(Long episodeId, String username);
    
    /**
     * 检查是否已收藏
     */
    boolean isFavorite(Long episodeId, String username);
    
    /**
     * 获取用户收藏列表
     */
    Page<FavoriteVO> getUserFavorites(String username, Pageable pageable);
    
    /**
     * 获取用户收藏列表
     */
    Page<FavoriteVO> getUserFavorites(Long userId, Pageable pageable);
    
    /**
     * 通过ID查找收藏
     */
    FavoriteVO findById(Long id);
    
    /**
     * 创建收藏
     */
    FavoriteVO create(FavoriteVO favoriteVO);
    
    /**
     * 更新收藏
     */
    FavoriteVO update(Long id, FavoriteVO favoriteVO);
    
    /**
     * 删除收藏
     */
    void delete(Long id);
} 