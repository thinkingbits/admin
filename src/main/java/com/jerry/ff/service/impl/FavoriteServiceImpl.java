package com.jerry.ff.service.impl;

import com.jerry.ff.exception.ResourceNotFoundException;
import com.jerry.ff.model.entity.Favorite;
import com.jerry.ff.model.entity.Movie;
import com.jerry.ff.model.entity.User;
import com.jerry.ff.model.vo.MovieVO;
import com.jerry.ff.repository.FavoriteRepository;
import com.jerry.ff.repository.MovieRepository;
import com.jerry.ff.repository.UserRepository;
import com.jerry.ff.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Override
    @Transactional
    public void addFavorite(String username, Long movieId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + movieId));
        
        // 检查是否已经收藏
        if (favoriteRepository.existsByUserIdAndMovieId(user.getId(), movieId)) {
            return;
        }
        
        Favorite favorite = Favorite.builder()
                .user(user)
                .media(movie)
                .createTime(LocalDateTime.now())
                .build();
        
        favoriteRepository.save(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(String username, Long movieId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        favoriteRepository.deleteByUserIdAndMovieId(user.getId(), movieId);
    }

    @Override
    public Page<MovieVO> getFavoriteMovies(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        Page<Movie> moviePage = favoriteRepository.findMoviesByUserId(user.getId(), pageable);
        
        List<MovieVO> movieVOs = moviePage.getContent().stream()
                .map(this::convertToMovieVO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(movieVOs, pageable, moviePage.getTotalElements());
    }

    @Override
    public boolean checkFavorite(String username, Long movieId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        return favoriteRepository.existsByUserIdAndMovieId(user.getId(), movieId);
    }
    
    private MovieVO convertToMovieVO(Movie movie) {
        return MovieVO.builder()
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
} 