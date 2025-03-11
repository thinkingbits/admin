package com.jerry.ff.service;

import com.jerry.ff.model.dto.FilmDTO;
import com.jerry.ff.model.vo.FilmVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FilmService {

    FilmVO createMovie(FilmDTO movieDTO);

    FilmVO updateMovie(Long id, FilmDTO movieDTO);

    void deleteMovie(Long id);

    FilmVO getMovie(Long id);

    Page<FilmVO> getAllMovies(Pageable pageable);

    Page<FilmVO> getMoviesByCategory(Long categoryId, Pageable pageable);

    Page<FilmVO> getActiveMovies(Pageable pageable);

    List<FilmVO> getTopRatedMovies(int limit);

    List<FilmVO> getLatestMovies(int limit);

    Page<FilmVO> searchMovies(String keyword, Pageable pageable);

    FilmVO updateMovieStatus(Long id, Integer status);

    Page<FilmVO> getMovies(Long categoryId, Pageable pageable);

    List<FilmVO> getFeaturedMovies(int limit);

    List<FilmVO> getPopularMovies(int limit);
} 