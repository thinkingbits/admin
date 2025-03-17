package com.jerry.ff.service;

import com.jerry.ff.model.vo.AgeVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AgeService {
    AgeVO create(AgeVO ageVO);
    AgeVO update(Long id, AgeVO ageVO);
    void delete(Long id);
    AgeVO findById(Long id);
    Page<AgeVO> findAll(Pageable pageable);
    List<AgeVO> findAll();
    boolean existsByName(String name);
    boolean existsByCode(String code);
} 