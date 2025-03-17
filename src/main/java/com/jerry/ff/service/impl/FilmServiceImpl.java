package com.jerry.ff.service.impl;

import com.jerry.ff.exception.BusinessException;
import com.jerry.ff.exception.ResourceNotFoundException;
import com.jerry.ff.model.dto.FilmDTO;
import com.jerry.ff.model.entity.Actor;
import com.jerry.ff.model.entity.Category;
import com.jerry.ff.model.entity.Director;
import com.jerry.ff.model.entity.Film;
import com.jerry.ff.model.vo.ActorVO;
import com.jerry.ff.model.vo.CategoryVO;
import com.jerry.ff.model.vo.DirectorVO;
import com.jerry.ff.model.vo.FilmVO;
import com.jerry.ff.repository.ActorRepository;
import com.jerry.ff.repository.CategoryRepository;
import com.jerry.ff.repository.DirectorRepository;
import com.jerry.ff.repository.FilmRepository;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final CategoryRepository categoryRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;

    @Override
    @Cacheable(value = "films", key = "'category_' + #categoryId + '_page_' + #pageable.pageNumber")
    public Page<FilmVO> getFilms(Long categoryId, Pageable pageable) {
        if (pageable == null) {
            throw new BusinessException("分页参数不能为空");
        }

        Page<Film> filmPage;
        if (categoryId != null) {
            filmPage = filmRepository.findByCategoryIdAndStatus(categoryId, 1, pageable);
        } else {
            filmPage = filmRepository.findByStatus(1, pageable);
        }
        
        return convertToFilmVOPage(filmPage);
    }

    @Override
    @Cacheable(value = "films", key = "'film_' + #id")
    public FilmVO getFilm(Long id) {
        if (id == null) {
            throw new BusinessException("电影ID不能为空");
        }

        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + id));
        
        return convertToFilmVO(film);
    }

    @Override
    @Transactional
    @CacheEvict(value = "films", allEntries = true)
    public FilmVO createFilm(FilmDTO filmDTO) {
        validateFilmDTO(filmDTO);

        Category category = getCategoryById(filmDTO.getCategoryId());
        List<Director> directors = getDirectorsByIds(filmDTO.getDirectorIds());
        List<Actor> actors = getActorsByIds(filmDTO.getActorIds());
        
        Film film = Film.builder()
                .type(filmDTO.getType())
                .title(filmDTO.getTitle().trim())
                .originalTitle(StringUtils.hasText(filmDTO.getOriginalTitle()) ? filmDTO.getOriginalTitle().trim() : null)
                .description(StringUtils.hasText(filmDTO.getDescription()) ? filmDTO.getDescription().trim() : null)
                .region(StringUtils.hasText(filmDTO.getRegion()) ? filmDTO.getRegion().trim() : null)
                .language(StringUtils.hasText(filmDTO.getLanguage()) ? filmDTO.getLanguage().trim() : null)
                .posterUrl(StringUtils.hasText(filmDTO.getPosterUrl()) ? filmDTO.getPosterUrl().trim() : null)
                .bannerUrl(StringUtils.hasText(filmDTO.getBannerUrl()) ? filmDTO.getBannerUrl().trim() : null)
                .videoUrl(StringUtils.hasText(filmDTO.getVideoUrl()) ? filmDTO.getVideoUrl().trim() : null)
                .airDate(filmDTO.getAirDate())
                .category(category)
                .directors(directors)
                .actors(actors)
                .status(filmDTO.getStatus() != null ? filmDTO.getStatus() : 0)
                .build();
        
        Film savedFilm = filmRepository.save(film);
        log.info("Created new film: {}", savedFilm.getTitle());
        return convertToFilmVO(savedFilm);
    }

    @Override
    @Transactional
    @CacheEvict(value = "films", allEntries = true)
    public FilmVO updateFilm(Long id, FilmDTO filmDTO) {
        if (id == null) {
            throw new BusinessException("电影ID不能为空");
        }
        validateFilmDTO(filmDTO);

        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + id));
        
        Category category = getCategoryById(filmDTO.getCategoryId());
        List<Director> directors = getDirectorsByIds(filmDTO.getDirectorIds());
        List<Actor> actors = getActorsByIds(filmDTO.getActorIds());
        
        film.setType(filmDTO.getType());
        film.setTitle(filmDTO.getTitle().trim());
        film.setOriginalTitle(StringUtils.hasText(filmDTO.getOriginalTitle()) ? filmDTO.getOriginalTitle().trim() : null);
        film.setDescription(StringUtils.hasText(filmDTO.getDescription()) ? filmDTO.getDescription().trim() : null);
        film.setRegion(StringUtils.hasText(filmDTO.getRegion()) ? filmDTO.getRegion().trim() : null);
        film.setLanguage(StringUtils.hasText(filmDTO.getLanguage()) ? filmDTO.getLanguage().trim() : null);
        film.setPosterUrl(StringUtils.hasText(filmDTO.getPosterUrl()) ? filmDTO.getPosterUrl().trim() : null);
        film.setBannerUrl(StringUtils.hasText(filmDTO.getBannerUrl()) ? filmDTO.getBannerUrl().trim() : null);
        film.setVideoUrl(StringUtils.hasText(filmDTO.getVideoUrl()) ? filmDTO.getVideoUrl().trim() : null);
        film.setAirDate(filmDTO.getAirDate());
        film.setCategory(category);
        film.setDirectors(directors);
        film.setActors(actors);
        film.setStatus(filmDTO.getStatus());
        
        Film updatedFilm = filmRepository.save(film);
        log.info("Updated film: {}", updatedFilm.getTitle());
        return convertToFilmVO(updatedFilm);
    }

    @Override
    @Transactional
    @CacheEvict(value = "films", allEntries = true)
    public void deleteFilm(Long id) {
        if (id == null) {
            throw new BusinessException("电影ID不能为空");
        }

        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + id));
        
        filmRepository.delete(film);
        log.info("Deleted film: {}", film.getTitle());
    }

    @Override
    @Transactional
    @CacheEvict(value = "films", allEntries = true)
    public FilmVO updateFilmStatus(Long id, Integer status) {
        if (id == null) {
            throw new BusinessException("电影ID不能为空");
        }

        if (status != 0 && status != 1) {
            throw new BusinessException("无效的状态值，状态必须为0或1");
        }
        
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + id));
        
        film.setStatus(status);
        Film updatedFilm = filmRepository.save(film);
        log.info("Updated film status: {} -> {}", film.getTitle(), status);
        return convertToFilmVO(updatedFilm);
    }

    @Override
    @Cacheable(value = "films", key = "'featured_' + #limit")
    public List<FilmVO> getFeaturedFilms(int limit) {
        if (limit <= 0) {
            return Collections.emptyList();
        }

        Pageable pageable = Pageable.ofSize(limit);
        List<Film> films = filmRepository.findByStatusOrderByRatingDesc(1, pageable);
        return films.stream().map(this::convertToFilmVO).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "films", key = "'latest_' + #limit")
    public List<FilmVO> getLatestFilms(int limit) {
        if (limit <= 0) {
            return Collections.emptyList();
        }

        Pageable pageable = Pageable.ofSize(limit);
        List<Film> films = filmRepository.findByStatusOrderByAirDateDesc(1, pageable);
        return films.stream().map(this::convertToFilmVO).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "films", key = "'popular_' + #limit")
    public List<FilmVO> getPopularFilms(int limit) {
        if (limit <= 0) {
            return Collections.emptyList();
        }

        Pageable pageable = Pageable.ofSize(limit);
        List<Film> films = filmRepository.findTopRatedFilms(pageable);
        return films.stream().map(this::convertToFilmVO).collect(Collectors.toList());
    }

    @Override
    public Page<FilmVO> searchFilms(String keyword, Pageable pageable) {
        if (pageable == null) {
            throw new BusinessException("分页参数不能为空");
        }

        if (!StringUtils.hasText(keyword)) {
            return getAllFilms(pageable);
        }

        Page<Film> filmPage = filmRepository.searchFilms(keyword.trim(), pageable);
        return convertToFilmVOPage(filmPage);
    }

    @Override
    public Page<FilmVO> getAllFilms(Pageable pageable) {
        if (pageable == null) {
            throw new BusinessException("分页参数不能为空");
        }

        Page<Film> filmPage = filmRepository.findAll(pageable);
        return convertToFilmVOPage(filmPage);
    }

    @Override
    public Page<FilmVO> getFilmsByCategory(Long categoryId, Pageable pageable) {
        if (categoryId == null) {
            throw new BusinessException("分类ID不能为空");
        }

        if (pageable == null) {
            throw new BusinessException("分页参数不能为空");
        }

        Page<Film> filmPage = filmRepository.findByCategoryId(categoryId, pageable);
        return convertToFilmVOPage(filmPage);
    }

    @Override
    public Page<FilmVO> getActiveFilms(Pageable pageable) {
        if (pageable == null) {
            throw new BusinessException("分页参数不能为空");
        }

        Page<Film> filmPage = filmRepository.findByStatus(1, pageable);
        return convertToFilmVOPage(filmPage);
    }

    @Override
    public List<FilmVO> getTopRatedFilms(int limit) {
        if (limit <= 0) {
            return Collections.emptyList();
        }

        Pageable pageable = Pageable.ofSize(limit);
        List<Film> films = filmRepository.findTopRatedFilms(pageable);
        return films.stream().map(this::convertToFilmVO).collect(Collectors.toList());
    }

    private void validateFilmDTO(FilmDTO filmDTO) {
        if (filmDTO == null) {
            throw new BusinessException("电影信息不能为空");
        }

        if (!StringUtils.hasText(filmDTO.getTitle())) {
            throw new BusinessException("电影标题不能为空");
        }

        if (filmDTO.getAirDate() == null) {
            throw new BusinessException("发行日期不能为空");
        }

        if (filmDTO.getCategoryId() == null) {
            throw new BusinessException("分类ID不能为空");
        }
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在，ID: " + categoryId));
    }

    private List<Director> getDirectorsByIds(List<Long> directorIds) {
        if (CollectionUtils.isEmpty(directorIds)) {
            return new ArrayList<>();
        }

        return directorIds.stream()
                .filter(Objects::nonNull)
                .map(id -> directorRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("导演不存在，ID: " + id)))
                .collect(Collectors.toList());
    }

    private List<Actor> getActorsByIds(List<Long> actorIds) {
        if (CollectionUtils.isEmpty(actorIds)) {
            return new ArrayList<>();
        }

        return actorIds.stream()
                .filter(Objects::nonNull)
                .map(id -> actorRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("演员不存在，ID: " + id)))
                .collect(Collectors.toList());
    }

    private FilmVO convertToFilmVO(Film film) {
        if (film == null) {
            return null;
        }

        return FilmVO.builder()
                .id(film.getId())
                .type(film.getType())
                .title(film.getTitle())
                .originalTitle(film.getOriginalTitle())
                .description(film.getDescription())
                .rating(film.getRating())
                .region(film.getRegion())
                .language(film.getLanguage())
                .posterUrl(film.getPosterUrl())
                .bannerUrl(film.getBannerUrl())
                .videoUrl(film.getVideoUrl())
                .airDate(film.getAirDate())
                .status(film.getStatus())
                .createAt(film.getCreateAt())
                .updateAt(film.getUpdateAt())
                .category(CategoryVO.builder()
                        .id(film.getCategory() != null ? film.getCategory().getId() : null)
                        .name(film.getCategory() != null ? film.getCategory().getName() : null)
                        .build())
                .directors(film.getDirectors().stream()
                        .map(director -> DirectorVO.builder()
                                .id(director.getId())
                                .name(director.getName())
                                .build())
                        .collect(Collectors.toList()))
                .actors(film.getActors().stream()
                        .map(actor -> ActorVO.builder()
                                .id(actor.getId())
                                .name(actor.getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private Page<FilmVO> convertToFilmVOPage(Page<Film> filmPage) {
        if (filmPage == null) {
            return Page.empty();
        }

        List<FilmVO> filmVOList = filmPage.getContent().stream()
                .map(this::convertToFilmVO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(filmVOList, filmPage.getPageable(), filmPage.getTotalElements());
    }
} 