package com.jerry.ff.service.impl;

import com.jerry.ff.model.entity.Language;
import com.jerry.ff.model.vo.LanguageVO;
import com.jerry.ff.repository.LanguageRepository;
import com.jerry.ff.service.LanguageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Override
    @Transactional
    public LanguageVO create(LanguageVO languageVO) {
        if (existsByName(languageVO.getName())) {
            throw new IllegalArgumentException("Language with name " + languageVO.getName() + " already exists");
        }
        if (existsByCode(languageVO.getCode())) {
            throw new IllegalArgumentException("Language with code " + languageVO.getCode() + " already exists");
        }
        Language language = new Language();
        BeanUtils.copyProperties(languageVO, language);
        language = languageRepository.save(language);
        BeanUtils.copyProperties(language, languageVO);
        return languageVO;
    }

    @Override
    @Transactional
    public LanguageVO update(Long id, LanguageVO languageVO) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with id: " + id));
        
        if (!language.getName().equals(languageVO.getName()) && existsByName(languageVO.getName())) {
            throw new IllegalArgumentException("Language with name " + languageVO.getName() + " already exists");
        }
        if (!language.getCode().equals(languageVO.getCode()) && existsByCode(languageVO.getCode())) {
            throw new IllegalArgumentException("Language with code " + languageVO.getCode() + " already exists");
        }

        BeanUtils.copyProperties(languageVO, language);
        language = languageRepository.save(language);
        BeanUtils.copyProperties(language, languageVO);
        return languageVO;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!languageRepository.existsById(id)) {
            throw new EntityNotFoundException("Language not found with id: " + id);
        }
        languageRepository.deleteById(id);
    }

    @Override
    public LanguageVO findById(Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with id: " + id));
        LanguageVO languageVO = new LanguageVO();
        BeanUtils.copyProperties(language, languageVO);
        return languageVO;
    }

    @Override
    public Page<LanguageVO> findAll(Pageable pageable) {
        return languageRepository.findAll(pageable)
                .map(language -> {
                    LanguageVO languageVO = new LanguageVO();
                    BeanUtils.copyProperties(language, languageVO);
                    return languageVO;
                });
    }

    @Override
    public List<LanguageVO> findAll() {
        return languageRepository.findAll().stream()
                .map(language -> {
                    LanguageVO languageVO = new LanguageVO();
                    BeanUtils.copyProperties(language, languageVO);
                    return languageVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return languageRepository.existsByName(name);
    }

    @Override
    public boolean existsByCode(String code) {
        return languageRepository.existsByCode(code);
    }
} 