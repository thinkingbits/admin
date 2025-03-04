package com.jerry.ff.service;

import com.jerry.ff.model.vo.DashboardVO;

import java.time.LocalDate;
import java.util.Map;

public interface StatisticsService {
    
    DashboardVO getDashboardStatistics();
    
    Map<String, Object> getOrderStatistics(LocalDate startDate, LocalDate endDate);
    
    Map<String, Object> getUserStatistics(LocalDate startDate, LocalDate endDate);
} 