package com.jerry.ff.service.impl;

import com.jerry.ff.exception.BusinessException;
import com.jerry.ff.exception.ResourceNotFoundException;
import com.jerry.ff.model.dto.FilmDTO;
import com.jerry.ff.model.entity.Category;
import com.jerry.ff.model.entity.Director;
import com.jerry.ff.model.entity.Film;
import com.jerry.ff.model.vo.FilmVO;
import com.jerry.ff.repository.CategoryRepository;
import com.jerry.ff.repository.DirectorRepository;
import com.jerry.ff.repository.MovieRepository;
import com.jerry.ff.service.FilmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieServiceImpl implements FilmService {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final DirectorRepository directorRepository;

    @Override
    @Cacheable(value = "movies", key = "'category_' + #categoryId + '_page_' + #pageable.pageNumber")
    public Page<FilmVO> getMovies(Long categoryId, Pageable pageable) {
        Page<Film> moviePage;
        
        if (categoryId != null) {
            moviePage = movieRepository.findByCategoryIdAndStatus(categoryId, 1, pageable);
        } else {
            moviePage = movieRepository.findByStatus(1, pageable);
        }

        return convertToMovieVOPage(moviePage);
    }

    @Override
    @Cacheable(value = "movies", key = "'movie_' + #id")
    public FilmVO getMovie(Long id) {
        Film movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + id));
        
        if (movie.getStatus() != 1) {
            throw new ResourceNotFoundException("电影未上架或已下架");
        }
        
        return convertToMovieVO(movie);
    }

    @Override
    @Transactional
    @CacheEvict(value = "movies", allEntries = true)
    public FilmVO createMovie(FilmDTO movieDTO) {
        Category category = categoryRepository.findById(movieDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在，ID: " + movieDTO.getCategoryId()));
        
        Set<Director> directors = new HashSet<>();
        if (movieDTO.getDirectorIds() != null && !movieDTO.getDirectorIds().isEmpty()) {
            directors = movieDTO.getDirectorIds().stream()
                    .map(directorId -> directorRepository.findById(directorId)
                            .orElseThrow(() -> new ResourceNotFoundException("导演不存在，ID: " + directorId)))
                    .collect(Collectors.toSet());
        }

        Film movie = Film.builder()
                .title(movieDTO.getTitle())
                .description(movieDTO.getDescription())
                .releaseDate(movieDTO.getReleaseDate())
                .duration(movieDTO.getDuration())
                .posterUrl(movieDTO.getPosterUrl())
                .videoUrl(movieDTO.getVideoUrl())
                .rating(0.0)
                .category(category)
                .status(1)
                .createTime(LocalDateTime.now())
                .build();
        
        Film savedMovie = movieRepository.save(movie);
        return convertToMovieVO(savedMovie);
    }

    @Override
    @Transactional
    @CacheEvict(value = "movies", allEntries = true)
    public FilmVO updateMovie(Long id, FilmDTO movieDTO) {
        Film movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + id));
        
        Category category = categoryRepository.findById(movieDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在，ID: " + movieDTO.getCategoryId()));
        
        Set<Director> directors = new HashSet<>();
        if (movieDTO.getDirectorIds() != null && !movieDTO.getDirectorIds().isEmpty()) {
            directors = movieDTO.getDirectorIds().stream()
                    .map(directorId -> directorRepository.findById(directorId)
                            .orElseThrow(() -> new ResourceNotFoundException("导演不存在，ID: " + directorId)))
                    .collect(Collectors.toSet());
        }
        
        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setDuration(movieDTO.getDuration());
        movie.setPosterUrl(movieDTO.getPosterUrl());
        movie.setVideoUrl(movieDTO.getVideoUrl());
        movie.setCategory(category);
        movie.setUpdateTime(LocalDateTime.now());
        
        Film updatedMovie = movieRepository.save(movie);
        return convertToMovieVO(updatedMovie);
    }

    @Override
    @Transactional
    @CacheEvict(value = "movies", allEntries = true)
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("电影不存在，ID: " + id);
        }
        
        movieRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "movies", allEntries = true)
    public FilmVO updateMovieStatus(Long id, Integer status) {
        if (status != 0 && status != 1) {
            throw new BusinessException(400, "电影状态值无效，应为0(下架)或1(上架)");
        }
        
        Film movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + id));
        
        movie.setStatus(status);
        movie.setUpdateTime(LocalDateTime.now());
        Film updatedMovie = movieRepository.save(movie);
        
        return convertToMovieVO(updatedMovie);
    }

    @Override
    @Cacheable(value = "movies", key = "'featured_' + #limit")
    public List<FilmVO> getFeaturedMovies(int limit) {
        // 这里可以根据具体逻辑获取推荐电影，这里简单返回最新上架的几部电影
        return movieRepository.findByStatusOrderByCreateTimeDesc(1, Pageable.ofSize(limit))
                .stream()
                .map(this::convertToMovieVO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "movies", key = "'latest_' + #limit")
    public List<FilmVO> getLatestMovies(int limit) {
        return movieRepository.findByStatusOrderByReleaseDateDesc(1, Pageable.ofSize(limit))
                .stream()
                .map(this::convertToMovieVO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "movies", key = "'popular_' + #limit")
    public List<FilmVO> getPopularMovies(int limit) {
        return movieRepository.findByStatusOrderByRatingDesc(1, Pageable.ofSize(limit))
                .stream()
                .map(this::convertToMovieVO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FilmVO> searchMovies(String keyword, Pageable pageable) {
        Page<Film> moviePage = movieRepository.findByTitleContainingAndStatus(keyword, 1, pageable);
        return convertToMovieVOPage(moviePage);
    }

    @Override
    public Page<FilmVO> getAllMovies(Pageable pageable) {
        Page<Film> moviePage = movieRepository.findAll(pageable);
        return convertToMovieVOPage(moviePage);
    }

    @Override
    public Page<FilmVO> getMoviesByCategory(Long categoryId, Pageable pageable) {
        Page<Film> moviePage = movieRepository.findByCategoryId(categoryId, pageable);
        return convertToMovieVOPage(moviePage);
    }

    @Override
    public Page<FilmVO> getActiveMovies(Pageable pageable) {
        Page<Film> moviePage = movieRepository.findByStatus(1, pageable);
        return convertToMovieVOPage(moviePage);
    }

    @Override
    public List<FilmVO> getTopRatedMovies(int limit) {
        return movieRepository.findByStatusOrderByRatingDesc(1, Pageable.ofSize(limit))
                .stream()
                .map(this::convertToMovieVO)
                .collect(Collectors.toList());
    }
    
    private FilmVO convertToMovieVO(Film movie) {
        return FilmVO.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .releaseDate(movie.getReleaseDate())
                .duration(movie.getDuration())
                .posterUrl(movie.getPosterUrl())
                .videoUrl(movie.getVideoUrl())
                .rating(movie.getRating())
                .categoryId(movie.getCategory().getId())
                .categoryName(movie.getCategory().getName())
                .status(movie.getStatus())
                .createTime(movie.getCreateTime())
                .updateTime(movie.getUpdateTime())
                .build();
    }
    
    private Page<FilmVO> convertToMovieVOPage(Page<Film> moviePage) {
        List<FilmVO> movieVOs = moviePage.getContent().stream()
                .map(this::convertToMovieVO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(movieVOs, moviePage.getPageable(), moviePage.getTotalElements());
    }
} 