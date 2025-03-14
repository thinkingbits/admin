package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Film, Long> {

} 