package com.jerry.ff.repository;

import com.jerry.ff.model.entity.UserDramaFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDramaFollowRepository extends JpaRepository<UserDramaFollow, Long> {
    List<UserDramaFollow> findByUserId(Long userId);
    boolean existsByUserIdAndDramaId(Long userId, Long dramaId);
} 