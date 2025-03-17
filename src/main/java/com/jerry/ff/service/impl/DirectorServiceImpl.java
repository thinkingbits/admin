package com.jerry.ff.service.impl;

import com.jerry.ff.exception.BusinessException;
import com.jerry.ff.exception.ResourceNotFoundException;
import com.jerry.ff.model.dto.DirectorDTO;
import com.jerry.ff.model.entity.Director;
import com.jerry.ff.model.vo.DirectorVO;
import com.jerry.ff.repository.DirectorRepository;
import com.jerry.ff.repository.FilmRepository;
import com.jerry.ff.service.DirectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;
    private final FilmRepository filmRepository;

    @Override
    @Transactional
    @CacheEvict(value = {"directors", "films"}, allEntries = true)
    public DirectorVO createDirector(DirectorDTO directorDTO) {
        validateDirectorDTO(directorDTO);

        String name = directorDTO.getName().trim();
        if (directorRepository.existsByName(name)) {
            throw new BusinessException("Director name already exists");
        }

        Director director = Director.builder()
                .name(name)
                .originalName(StringUtils.hasText(directorDTO.getOriginalName()) ? directorDTO.getOriginalName().trim() : null)
                .photoUrl(StringUtils.hasText(directorDTO.getPhotoUrl()) ? directorDTO.getPhotoUrl().trim() : null)
                .biography(StringUtils.hasText(directorDTO.getBiography()) ? directorDTO.getBiography().trim() : null)
                .build();

        Director savedDirector = directorRepository.save(director);
        log.info("Created new director: {}", savedDirector.getName());
        return convertToDirectorVO(savedDirector);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"directors", "films"}, allEntries = true)
    public DirectorVO updateDirector(Long id, DirectorDTO directorDTO) {
        validateDirectorDTO(directorDTO);
        validateId(id);

        Director director = getDirectorById(id);

        String newName = directorDTO.getName().trim();
        if (!director.getName().equals(newName) && directorRepository.existsByName(newName)) {
            throw new BusinessException("Director name already exists");
        }

        director.setName(newName);
        director.setOriginalName(StringUtils.hasText(directorDTO.getOriginalName()) ? directorDTO.getOriginalName().trim() : null);
        director.setPhotoUrl(StringUtils.hasText(directorDTO.getPhotoUrl()) ? directorDTO.getPhotoUrl().trim() : null);
        director.setBiography(StringUtils.hasText(directorDTO.getBiography()) ? directorDTO.getBiography().trim() : null);

        Director updatedDirector = directorRepository.save(director);
        log.info("Updated director: {}", updatedDirector.getName());
        return convertToDirectorVO(updatedDirector);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"directors", "films"}, allEntries = true)
    public void deleteDirector(Long id) {
        validateId(id);

        Director director = getDirectorById(id);

        if (filmRepository.existsByDirectorsId(id)) {
            throw new BusinessException("Cannot delete director with associated films");
        }

        directorRepository.delete(director);
        log.info("Deleted director: {}", director.getName());
    }

    @Override
    public List<DirectorVO> getPopularDirectors(int limit) {
        return List.of();
    }

    @Override
    @Cacheable(value = "directors", key = "'director_' + #id", unless = "#result == null")
    public DirectorVO getDirector(Long id) {
        validateId(id);
        return convertToDirectorVO(getDirectorById(id));
    }

    @Override
    @Cacheable(value = "directors", key = "'all_page_' + #pageable.pageNumber + '_' + #pageable.pageSize", unless = "#result.content.isEmpty()")
    public Page<DirectorVO> getAllDirectors(Pageable pageable) {
        validatePageable(pageable);
        Page<Director> directorPage = directorRepository.findAll(pageable);
        return convertToDirectorVOPage(directorPage);
    }

    @Cacheable(value = "directors", key = "'search_' + #keyword + '_page_' + #pageable.pageNumber + '_' + #pageable.pageSize", 
               condition = "#keyword != null", unless = "#result.content.isEmpty()")
    public Page<DirectorVO> searchDirectors(String keyword, Pageable pageable) {
        validatePageable(pageable);

        if (!StringUtils.hasText(keyword)) {
            return getAllDirectors(pageable);
        }

        String trimmedKeyword = keyword.trim();
        Page<Director> directorPage = directorRepository.searchDirectors(trimmedKeyword, pageable);
        
        if (directorPage.isEmpty()) {
            // 如果精确搜索没有结果，尝试模糊搜索
            directorPage = directorRepository.findByNameContainingOrOriginalNameContaining(
                trimmedKeyword, trimmedKeyword, pageable);
        }
        
        return convertToDirectorVOPage(directorPage);
    }

    @Cacheable(value = "directors", key = "'film_' + #filmId", unless = "#result.isEmpty()")
    public List<DirectorVO> getDirectorsByFilm(Long filmId) {
        validateId(filmId, "Film ID");

        List<Director> directors = directorRepository.findByFilmId(filmId);
        return directors.stream()
                .map(this::convertToDirectorVO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "directors", key = "'ids_' + T(java.util.Arrays).toString(#ids.toArray())", 
               condition = "#ids != null && !#ids.isEmpty()", unless = "#result.isEmpty()")
    public List<DirectorVO> getDirectorsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> validIds = ids.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (validIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Director> directors = directorRepository.findByIdIn(validIds);
        return directors.stream()
                .map(this::convertToDirectorVO)
                .collect(Collectors.toList());
    }

    private void validateDirectorDTO(DirectorDTO directorDTO) {
        if (directorDTO == null) {
            throw new BusinessException("Director information cannot be empty");
        }

        if (!StringUtils.hasText(directorDTO.getName())) {
            throw new BusinessException("Director name cannot be empty");
        }

        if (directorDTO.getName().length() > 100) {
            throw new BusinessException("Director name cannot exceed 100 characters");
        }

        if (StringUtils.hasText(directorDTO.getBiography()) && directorDTO.getBiography().length() > 500) {
            throw new BusinessException("Director description cannot exceed 500 characters");
        }
    }

    private void validateId(Long id) {
        validateId(id, "Director ID");
    }

    private void validateId(Long id, String fieldName) {
        if (id == null) {
            throw new BusinessException(fieldName + " cannot be empty");
        }
        if (id <= 0) {
            throw new BusinessException(fieldName + " invalid");
        }
    }

    private void validatePageable(Pageable pageable) {
        if (pageable == null) {
            throw new BusinessException("Page parameters cannot be empty");
        }
        if (pageable.getPageSize() > 100) {
            throw new BusinessException("Page size cannot exceed 100");
        }
    }

    private Director getDirectorById(Long id) {
        return directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director not found, ID: " + id));
    }

    private DirectorVO convertToDirectorVO(Director director) {
        if (director == null) {
            return null;
        }

        return DirectorVO.builder()
                .id(director.getId())
                .name(director.getName())
                .originalName(director.getOriginalName())
                .avatarUrl(director.getPhotoUrl())
                .biography(director.getBiography())
                .build();
    }

    private Page<DirectorVO> convertToDirectorVOPage(Page<Director> directorPage) {
        if (directorPage == null) {
            return Page.empty();
        }

        List<DirectorVO> directorVOList = directorPage.getContent().stream()
                .map(this::convertToDirectorVO)
                .collect(Collectors.toList());

        return new PageImpl<>(directorVOList, directorPage.getPageable(), directorPage.getTotalElements());
    }
} 