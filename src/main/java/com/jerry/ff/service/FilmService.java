package com.jerry.ff.service;

import com.jerry.ff.model.dto.FilmDTO;
import com.jerry.ff.model.vo.FilmVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FilmService {

    FilmVO createFilm(FilmDTO filmDTO);

    FilmVO updateFilm(Long id, FilmDTO filmDTO);

    void deleteFilm(Long id);

    FilmVO getFilm(Long id);

    Page<FilmVO> getAllFilms(Pageable pageable);

    Page<FilmVO> getFilmsByCategory(Long categoryId, Pageable pageable);

    Page<FilmVO> getActiveFilms(Pageable pageable);

    List<FilmVO> getTopRatedFilms(int limit);

    List<FilmVO> getLatestFilms(int limit);

    Page<FilmVO> searchFilms(String keyword, Pageable pageable);

    FilmVO updateFilmStatus(Long id, Integer status);

    Page<FilmVO> getFilms(Long categoryId, Pageable pageable);

    List<FilmVO> getFeaturedFilms(int limit);

    List<FilmVO> getPopularFilms(int limit);
} 