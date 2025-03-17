package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
} 