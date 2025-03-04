package com.jerry.ff.service.impl;

import com.jerry.ff.exception.ResourceNotFoundException;
import com.jerry.ff.model.dto.DirectorDTO;
import com.jerry.ff.model.entity.Director;
import com.jerry.ff.model.vo.DirectorVO;
import com.jerry.ff.repository.DirectorRepository;
import com.jerry.ff.repository.MovieRepository;
import com.jerry.ff.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;
    private final MovieRepository movieRepository;

    @Override
    @Cacheable(value = "directors", key = "'all_page_' + #pageable.pageNumber")
    public Page<DirectorVO> getAllDirectors(Pageable pageable) {
        Page<Director> directorPage = directorRepository.findAll(pageable);
        return convertToDirectorVOPage(directorPage);
    }

    @Override
    @Cacheable(value = "directors", key = "'director_' + #id")
    public DirectorVO getDirector(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("导演不存在，ID: " + id));
        
        return convertToDirectorVO(director);
    }

    @Override
    @Transactional
    @CacheEvict(value = "directors", allEntries = true)
    public DirectorVO createDirector(DirectorDTO directorDTO) {
        Director director = Director.builder()
                .name(directorDTO.getName())
                .biography(directorDTO.getBiography())
                .photoUrl(directorDTO.getPhotoUrl())
                .createTime(LocalDateTime.now())
                .build();
        
        Director savedDirector = directorRepository.save(director);
        return convertToDirectorVO(savedDirector);
    }

    @Override
    @Transactional
    @CacheEvict(value = "directors", allEntries = true)
    public DirectorVO updateDirector(Long id, DirectorDTO directorDTO) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("导演不存在，ID: " + id));
        
        director.setName(directorDTO.getName());
        director.setBiography(directorDTO.getBiography());
        
        if (directorDTO.getPhotoUrl() != null && !directorDTO.getPhotoUrl().isEmpty()) {
            director.setPhotoUrl(directorDTO.getPhotoUrl());
        }
        
        director.setUpdateTime(LocalDateTime.now());
        Director updatedDirector = directorRepository.save(director);
        
        return convertToDirectorVO(updatedDirector);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"directors", "movies"}, allEntries = true)
    public void deleteDirector(Long id) {
        if (!directorRepository.existsById(id)) {
            throw new ResourceNotFoundException("导演不存在，ID: " + id);
        }
        
        // 检查是否有电影关联了该导演
        if (movieRepository.existsByDirectorsId(id)) {
            throw new RuntimeException("无法删除导演，有电影与其关联");
        }
        
        directorRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "directors", key = "'popular_' + #limit")
    public List<DirectorVO> getPopularDirectors(int limit) {
        // 这里可以根据电影数量或者评分来确定热门导演
        List<Director> directors = directorRepository.findPopularDirectors(limit);
        return directors.stream()
                .map(this::convertToDirectorVO)
                .collect(Collectors.toList());
    }
    
    private DirectorVO convertToDirectorVO(Director director) {
        return DirectorVO.builder()
                .id(director.getId())
                .name(director.getName())
                .biography(director.getBiography())
                .photoUrl(director.getPhotoUrl())
                .build();
    }
    
    private Page<DirectorVO> convertToDirectorVOPage(Page<Director> directorPage) {
        List<DirectorVO> directorVOs = directorPage.getContent().stream()
                .map(this::convertToDirectorVO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(directorVOs, directorPage.getPageable(), directorPage.getTotalElements());
    }
} 