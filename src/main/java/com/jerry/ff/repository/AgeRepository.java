package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Age;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgeRepository extends JpaRepository<Age, Long> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
} 