package com.jerry.ff.service.impl;

import com.jerry.ff.model.entity.Age;
import com.jerry.ff.model.vo.AgeVO;
import com.jerry.ff.repository.AgeRepository;
import com.jerry.ff.service.AgeService;
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
public class AgeServiceImpl implements AgeService {

    private final AgeRepository ageRepository;

    @Override
    @Transactional
    public AgeVO create(AgeVO ageVO) {
        if (existsByName(ageVO.getName())) {
            throw new IllegalArgumentException("Age with name " + ageVO.getName() + " already exists");
        }
        if (existsByCode(ageVO.getCode())) {
            throw new IllegalArgumentException("Age with code " + ageVO.getCode() + " already exists");
        }
        Age age = new Age();
        BeanUtils.copyProperties(ageVO, age);
        age = ageRepository.save(age);
        BeanUtils.copyProperties(age, ageVO);
        return ageVO;
    }

    @Override
    @Transactional
    public AgeVO update(Long id, AgeVO ageVO) {
        Age age = ageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Age not found with id: " + id));
        
        if (!age.getName().equals(ageVO.getName()) && existsByName(ageVO.getName())) {
            throw new IllegalArgumentException("Age with name " + ageVO.getName() + " already exists");
        }
        if (!age.getCode().equals(ageVO.getCode()) && existsByCode(ageVO.getCode())) {
            throw new IllegalArgumentException("Age with code " + ageVO.getCode() + " already exists");
        }

        BeanUtils.copyProperties(ageVO, age);
        age = ageRepository.save(age);
        BeanUtils.copyProperties(age, ageVO);
        return ageVO;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!ageRepository.existsById(id)) {
            throw new EntityNotFoundException("Age not found with id: " + id);
        }
        ageRepository.deleteById(id);
    }

    @Override
    public AgeVO findById(Long id) {
        Age age = ageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Age not found with id: " + id));
        AgeVO ageVO = new AgeVO();
        BeanUtils.copyProperties(age, ageVO);
        return ageVO;
    }

    @Override
    public Page<AgeVO> findAll(Pageable pageable) {
        return ageRepository.findAll(pageable)
                .map(age -> {
                    AgeVO ageVO = new AgeVO();
                    BeanUtils.copyProperties(age, ageVO);
                    return ageVO;
                });
    }

    @Override
    public List<AgeVO> findAll() {
        return ageRepository.findAll().stream()
                .map(age -> {
                    AgeVO ageVO = new AgeVO();
                    BeanUtils.copyProperties(age, ageVO);
                    return ageVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return ageRepository.existsByName(name);
    }

    @Override
    public boolean existsByCode(String code) {
        return ageRepository.existsByCode(code);
    }
} 