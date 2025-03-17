package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
} 