package com.jerry.ff.service;

import com.jerry.ff.model.entity.Language;
import com.jerry.ff.model.vo.LanguageVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LanguageService {
    LanguageVO create(LanguageVO languageVO);
    LanguageVO update(Long id, LanguageVO languageVO);
    void delete(Long id);
    LanguageVO findById(Long id);
    Page<LanguageVO> findAll(Pageable pageable);
    List<LanguageVO> findAll();
    boolean existsByName(String name);
    boolean existsByCode(String code);
} 