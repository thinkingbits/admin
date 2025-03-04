package com.jerry.ff.repository;

import com.jerry.ff.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    long countByRegisterTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT DATE(u.registerTime) as date, COUNT(u) as count FROM User u " +
           "WHERE u.registerTime BETWEEN :start AND :end GROUP BY DATE(u.registerTime)")
    List<Object[]> countUsersByDayBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
} 