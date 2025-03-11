package com.jerry.ff.service.impl;

import com.jerry.ff.exception.ResourceNotFoundException;
import com.jerry.ff.model.entity.Category;
import com.jerry.ff.model.entity.Film;
import com.jerry.ff.model.entity.MovieSeries;
import com.jerry.ff.model.vo.FilmSeriesVO;
import com.jerry.ff.repository.MovieRepository;
import com.jerry.ff.repository.FilmSeriesRepository;
import com.jerry.ff.service.FilmSeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieSeriesServiceImpl implements FilmSeriesService {

    private final FilmSeriesRepository movieSeriesRepository;
    private final MovieRepository movieRepository;

    @Override
    public FilmSeriesVO getMovieSeries(Long id) {
        MovieSeries movieSeries = movieSeriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("电影系列不存在，ID: " + id));
        return convertToMovieSeriesVO(movieSeries);
    }

    @Override
    public Page<FilmSeriesVO> getMovieSeriesList(Long categoryId, Pageable pageable) {
        Page<MovieSeries> movieSeriesPage;
        if (categoryId != null) {
            movieSeriesPage = movieSeriesRepository.findByCategoryId(categoryId, pageable);
        } else {
            movieSeriesPage = movieSeriesRepository.findByStatus(1, pageable);
        }
        return convertToMovieSeriesVOPage(movieSeriesPage);
    }

    @Override
    public Page<FilmSeriesVO> getActiveMovieSeries(Pageable pageable) {
        return convertToMovieSeriesVOPage(movieSeriesRepository.findByStatus(1, pageable));
    }

    @Override
    public List<FilmSeriesVO> getLatestMovieSeries(int limit) {
        return movieSeriesRepository.findLatestMovieSeries(Pageable.ofSize(limit))
                .stream()
                .map(this::convertToMovieSeriesVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FilmSeriesVO> getTopRatedMovieSeries(int limit) {
        return movieSeriesRepository.findByStatusOrderByRatingDesc(1, Pageable.ofSize(limit))
                .stream()
                .map(this::convertToMovieSeriesVO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FilmSeriesVO> searchMovieSeries(String keyword, Pageable pageable) {
        return convertToMovieSeriesVOPage(
                movieSeriesRepository.findByTitleContainingAndStatus(keyword, 1, pageable));
    }

    @Override
    @Transactional
    public FilmSeriesVO createMovieSeries(FilmSeriesVO movieSeriesVO) {
        MovieSeries movieSeries = MovieSeries.builder()
                .title(movieSeriesVO.getTitle())
                .originalTitle(movieSeriesVO.getOriginalTitle())
                .description(movieSeriesVO.getDescription())
                .posterUrl(movieSeriesVO.getPosterUrl())
                .bannerUrl(movieSeriesVO.getBannerUrl())
                .startDate(movieSeriesVO.getStartDate())
                .endDate(movieSeriesVO.getEndDate())
                .totalMovies(movieSeriesVO.getTotalMovies())
                .rating(0.0)
                .ratingCount(0)
                .category(movieSeriesVO.getCategoryId() != null ? 
                        Category.builder().id(movieSeriesVO.getCategoryId()).build() : null)
                .region(movieSeriesVO.getRegion())
                .language(movieSeriesVO.getLanguage())
                .status(1)
                .build();

        return convertToMovieSeriesVO(movieSeriesRepository.save(movieSeries));
    }

    @Override
    @Transactional
    public FilmSeriesVO updateMovieSeries(Long id, FilmSeriesVO movieSeriesVO) {
        MovieSeries movieSeries = movieSeriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("电影系列不存在，ID: " + id));

        movieSeries.setTitle(movieSeriesVO.getTitle());
        movieSeries.setOriginalTitle(movieSeriesVO.getOriginalTitle());
        movieSeries.setDescription(movieSeriesVO.getDescription());
        movieSeries.setPosterUrl(movieSeriesVO.getPosterUrl());
        movieSeries.setBannerUrl(movieSeriesVO.getBannerUrl());
        movieSeries.setStartDate(movieSeriesVO.getStartDate());
        movieSeries.setEndDate(movieSeriesVO.getEndDate());
        movieSeries.setTotalMovies(movieSeriesVO.getTotalMovies());
        if (movieSeriesVO.getCategoryId() != null) {
            movieSeries.setCategory(Category.builder().id(movieSeriesVO.getCategoryId()).build());
        }
        movieSeries.setRegion(movieSeriesVO.getRegion());
        movieSeries.setLanguage(movieSeriesVO.getLanguage());
        movieSeries.setStatus(movieSeriesVO.getStatus());

        return convertToMovieSeriesVO(movieSeriesRepository.save(movieSeries));
    }

    @Override
    @Transactional
    public void deleteMovieSeries(Long id) {
        if (!movieSeriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("电影系列不存在，ID: " + id);
        }
        movieSeriesRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addMovieToSeries(Long seriesId, Long movieId, Integer seriesOrder) {
        MovieSeries movieSeries = movieSeriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException("电影系列不存在，ID: " + seriesId));
        
        Film movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + movieId));

//        movie.setMovieSeries(movieSeries);
        movie.setSeriesOrder(seriesOrder);
        movieRepository.save(movie);

        // 更新系列总电影数
        movieSeries.setTotalMovies(movieSeries.getMovies().size());
        movieSeriesRepository.save(movieSeries);
    }

    @Override
    @Transactional
    public void removeMovieFromSeries(Long seriesId, Long movieId) {
        Film movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + movieId));

//        if (!seriesId.equals(movie.getMovieSeries().getId())) {
//            throw new IllegalArgumentException("该电影不属于指定的系列");
//        }

//        movie.setMovieSeries(null);
        movie.setSeriesOrder(null);
        movieRepository.save(movie);

        // 更新系列总电影数
        MovieSeries movieSeries = movieSeriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException("电影系列不存在，ID: " + seriesId));
        movieSeries.setTotalMovies(movieSeries.getMovies().size());
        movieSeriesRepository.save(movieSeries);
    }

    @Override
    @Transactional
    public void updateMovieSeriesOrder(Long seriesId, Long movieId, Integer newOrder) {
        Film movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + movieId));

//        if (!seriesId.equals(movie.getMovieSeries().getId())) {
//            throw new IllegalArgumentException("该电影不属于指定的系列");
//        }

        movie.setSeriesOrder(newOrder);
        movieRepository.save(movie);
    }

    private FilmSeriesVO convertToMovieSeriesVO(MovieSeries movieSeries) {
        return FilmSeriesVO.builder()
                .id(movieSeries.getId())
                .title(movieSeries.getTitle())
                .originalTitle(movieSeries.getOriginalTitle())
                .description(movieSeries.getDescription())
                .posterUrl(movieSeries.getPosterUrl())
                .bannerUrl(movieSeries.getBannerUrl())
                .startDate(movieSeries.getStartDate())
                .endDate(movieSeries.getEndDate())
                .totalMovies(movieSeries.getTotalMovies())
                .rating(movieSeries.getRating())
                .ratingCount(movieSeries.getRatingCount())
                .categoryId(movieSeries.getCategory() != null ? movieSeries.getCategory().getId() : null)
                .categoryName(movieSeries.getCategory() != null ? movieSeries.getCategory().getName() : null)
                .region(movieSeries.getRegion())
                .language(movieSeries.getLanguage())
                .status(movieSeries.getStatus())
                .createTime(movieSeries.getCreateTime())
                .updateTime(movieSeries.getUpdateTime())
                .movies(movieSeries.getMovies() != null ?
                        movieSeries.getMovies().stream()
                                .sorted(Comparator.comparing(Film::getSeriesOrder))
                                .map(movie -> FilmSeriesVO.MovieInSeriesVO.builder()
                                        .id(movie.getId())
                                        .title(movie.getTitle())
                                        .description(movie.getDescription())
                                        .releaseDate(movie.getReleaseDate())
                                        .duration(movie.getDuration())
                                        .posterUrl(movie.getPosterUrl())
                                        .rating(movie.getRating())
                                        .seriesOrder(movie.getSeriesOrder())
                                        .build())
                                .collect(Collectors.toList()) : null)
                .build();
    }

    private Page<FilmSeriesVO> convertToMovieSeriesVOPage(Page<MovieSeries> movieSeriesPage) {
        List<FilmSeriesVO> movieSeriesVOs = movieSeriesPage.getContent().stream()
                .map(this::convertToMovieSeriesVO)
                .collect(Collectors.toList());

        return new PageImpl<>(movieSeriesVOs, movieSeriesPage.getPageable(), movieSeriesPage.getTotalElements());
    }
} 