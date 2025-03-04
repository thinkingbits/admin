package com.jerry.ff.controller;

import com.jerry.ff.model.vo.DashboardVO;
import com.jerry.ff.service.StatisticsService;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/statistics")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "统计分析", description = "数据统计和分析相关接口")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    @Operation(summary = "获取控制面板数据", description = "获取控制面板统计数据")
    public ResponseResult<DashboardVO> getDashboardStatistics() {
        return ResponseResult.success(statisticsService.getDashboardStatistics());
    }

    @GetMapping("/orders")
    @Operation(summary = "获取订单统计", description = "获取指定时间范围内的订单统计数据")
    public ResponseResult<Object> getOrderStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return ResponseResult.success(statisticsService.getOrderStatistics(startDate, endDate));
    }

    @GetMapping("/users")
    @Operation(summary = "获取用户统计", description = "获取指定时间范围内的用户注册统计数据")
    public ResponseResult<Object> getUserStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return ResponseResult.success(statisticsService.getUserStatistics(startDate, endDate));
    }
} 