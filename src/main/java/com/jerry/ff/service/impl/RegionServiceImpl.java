package com.jerry.ff.service.impl;

import com.jerry.ff.model.entity.Region;
import com.jerry.ff.model.vo.RegionVO;
import com.jerry.ff.repository.RegionRepository;
import com.jerry.ff.service.RegionService;
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
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    @Transactional
    public RegionVO create(RegionVO regionVO) {
        if (existsByName(regionVO.getName())) {
            throw new IllegalArgumentException("Region with name " + regionVO.getName() + " already exists");
        }
        if (existsByCode(regionVO.getCode())) {
            throw new IllegalArgumentException("Region with code " + regionVO.getCode() + " already exists");
        }
        Region region = new Region();
        BeanUtils.copyProperties(regionVO, region);
        region = regionRepository.save(region);
        BeanUtils.copyProperties(region, regionVO);
        return regionVO;
    }

    @Override
    @Transactional
    public RegionVO update(Long id, RegionVO regionVO) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + id));
        
        if (!region.getName().equals(regionVO.getName()) && existsByName(regionVO.getName())) {
            throw new IllegalArgumentException("Region with name " + regionVO.getName() + " already exists");
        }
        if (!region.getCode().equals(regionVO.getCode()) && existsByCode(regionVO.getCode())) {
            throw new IllegalArgumentException("Region with code " + regionVO.getCode() + " already exists");
        }

        BeanUtils.copyProperties(regionVO, region);
        region = regionRepository.save(region);
        BeanUtils.copyProperties(region, regionVO);
        return regionVO;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new EntityNotFoundException("Region not found with id: " + id);
        }
        regionRepository.deleteById(id);
    }

    @Override
    public RegionVO findById(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + id));
        RegionVO regionVO = new RegionVO();
        BeanUtils.copyProperties(region, regionVO);
        return regionVO;
    }

    @Override
    public Page<RegionVO> findAll(Pageable pageable) {
        return regionRepository.findAll(pageable)
                .map(region -> {
                    RegionVO regionVO = new RegionVO();
                    BeanUtils.copyProperties(region, regionVO);
                    return regionVO;
                });
    }

    @Override
    public List<RegionVO> findAll() {
        return regionRepository.findAll().stream()
                .map(region -> {
                    RegionVO regionVO = new RegionVO();
                    BeanUtils.copyProperties(region, regionVO);
                    return regionVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return regionRepository.existsByName(name);
    }

    @Override
    public boolean existsByCode(String code) {
        return regionRepository.existsByCode(code);
    }
} 