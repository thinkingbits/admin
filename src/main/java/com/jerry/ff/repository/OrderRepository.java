package com.jerry.ff.repository;

import com.jerry.ff.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNo(String orderNo);
    
    Page<Order> findByUserId(Long userId, Pageable pageable);
    
    Page<Order> findByStatus(Integer status, Pageable pageable);
    
    Page<Order> findByCreateAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    long countByCreateAtBetween(LocalDateTime start, LocalDateTime end);
    
    long countByStatus(Integer status);
    
    @Query("SELECT SUM(o.amount) FROM Order o WHERE o.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") Integer status);
    
    
    @Query("SELECT SUM(o.amount) FROM Order o WHERE o.status = :status AND o.createAt BETWEEN :start AND :end")
    BigDecimal sumAmountByStatusAndCreateAtBetween(
            @Param("status") Integer status, 
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
            
    @Query("SELECT DATE(o.createAt) as date, COUNT(o) as count FROM Order o " +
           "WHERE o.createAt BETWEEN :start AND :end GROUP BY DATE(o.createAt)")
    List<Object[]> countOrdersByDayBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
            
    @Query("SELECT DATE(o.createAt) as date, SUM(o.amount) as revenue FROM Order o " +
           "WHERE o.status = 1 AND o.createAt BETWEEN :start AND :end GROUP BY DATE(o.createAt)")
    List<Object[]> sumRevenueByDayBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
} 