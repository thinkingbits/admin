package com.jerry.ff.service;

import com.jerry.ff.model.dto.MovieDTO;
import com.jerry.ff.model.vo.MovieVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

    MovieVO createMovie(MovieDTO movieDTO);

    MovieVO updateMovie(Long id, MovieDTO movieDTO);

    void deleteMovie(Long id);

    MovieVO getMovie(Long id);

    Page<MovieVO> getAllMovies(Pageable pageable);

    Page<MovieVO> getMoviesByCategory(Long categoryId, Pageable pageable);

    Page<MovieVO> getActiveMovies(Pageable pageable);

    List<MovieVO> getTopRatedMovies(int limit);

    List<MovieVO> getLatestMovies(int limit);

    Page<MovieVO> searchMovies(String keyword, Pageable pageable);

    MovieVO updateMovieStatus(Long id, Integer status);

    Page<MovieVO> getMovies(Long categoryId, Pageable pageable);

    List<MovieVO> getFeaturedMovies(int limit);

    List<MovieVO> getPopularMovies(int limit);
} 