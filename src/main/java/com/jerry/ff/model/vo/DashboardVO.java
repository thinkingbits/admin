package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardVO implements Serializable {
    
    private Long totalUsers;
    private Long totalMovies;
    private Long totalComments;
    private Long totalOrders;
    private Long pendingOrders;
    private Long pendingComments;
    private BigDecimal totalRevenue;
    private Long todayNewUsers;
    private Long todayNewOrders;
    private BigDecimal todayRevenue;
} 