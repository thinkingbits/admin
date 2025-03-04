package com.jerry.ff.service;

import com.jerry.ff.model.vo.MovieSeriesVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieSeriesService {
    MovieSeriesVO getMovieSeries(Long id);
    
    Page<MovieSeriesVO> getMovieSeriesList(Long categoryId, Pageable pageable);
    
    Page<MovieSeriesVO> getActiveMovieSeries(Pageable pageable);
    
    List<MovieSeriesVO> getLatestMovieSeries(int limit);
    
    List<MovieSeriesVO> getTopRatedMovieSeries(int limit);
    
    Page<MovieSeriesVO> searchMovieSeries(String keyword, Pageable pageable);
    
    MovieSeriesVO createMovieSeries(MovieSeriesVO movieSeriesVO);
    
    MovieSeriesVO updateMovieSeries(Long id, MovieSeriesVO movieSeriesVO);
    
    void deleteMovieSeries(Long id);
    
    void addMovieToSeries(Long seriesId, Long movieId, Integer seriesOrder);
    
    void removeMovieFromSeries(Long seriesId, Long movieId);
    
    void updateMovieSeriesOrder(Long seriesId, Long movieId, Integer newOrder);
} 