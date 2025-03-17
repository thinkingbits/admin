package com.jerry.ff.service;

import com.jerry.ff.model.entity.Region;
import com.jerry.ff.model.vo.RegionVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RegionService {
    RegionVO create(RegionVO regionVO);
    RegionVO update(Long id, RegionVO regionVO);
    void delete(Long id);
    RegionVO findById(Long id);
    Page<RegionVO> findAll(Pageable pageable);
    List<RegionVO> findAll();
    boolean existsByName(String name);
    boolean existsByCode(String code);
} 