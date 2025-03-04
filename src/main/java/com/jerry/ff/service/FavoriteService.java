package com.jerry.ff.service;

import com.jerry.ff.model.vo.MovieVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteService {
    
    void addFavorite(String username, Long movieId);
    
    void removeFavorite(String username, Long movieId);
    
    Page<MovieVO> getFavoriteMovies(String username, Pageable pageable);
    
    boolean checkFavorite(String username, Long movieId);
} 