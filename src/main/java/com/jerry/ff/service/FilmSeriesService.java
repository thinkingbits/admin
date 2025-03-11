package com.jerry.ff.service;

import com.jerry.ff.model.vo.FilmSeriesVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FilmSeriesService {
    FilmSeriesVO getMovieSeries(Long id);
    
    Page<FilmSeriesVO> getMovieSeriesList(Long categoryId, Pageable pageable);
    
    Page<FilmSeriesVO> getActiveMovieSeries(Pageable pageable);
    
    List<FilmSeriesVO> getLatestMovieSeries(int limit);
    
    List<FilmSeriesVO> getTopRatedMovieSeries(int limit);
    
    Page<FilmSeriesVO> searchMovieSeries(String keyword, Pageable pageable);
    
    FilmSeriesVO createMovieSeries(FilmSeriesVO movieSeriesVO);
    
    FilmSeriesVO updateMovieSeries(Long id, FilmSeriesVO movieSeriesVO);
    
    void deleteMovieSeries(Long id);
    
    void addMovieToSeries(Long seriesId, Long movieId, Integer seriesOrder);
    
    void removeMovieFromSeries(Long seriesId, Long movieId);
    
    void updateMovieSeriesOrder(Long seriesId, Long movieId, Integer newOrder);
} 