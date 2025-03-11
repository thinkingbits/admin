package com.jerry.ff.service.impl;

import com.jerry.ff.model.vo.DashboardVO;
import com.jerry.ff.repository.CommentRepository;
import com.jerry.ff.repository.MovieRepository;
import com.jerry.ff.repository.OrderRepository;
import com.jerry.ff.repository.UserRepository;
import com.jerry.ff.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
//    private final CommentRepository commentRepository;
    private final OrderRepository orderRepository;

    @Override
    public DashboardVO getDashboardStatistics() {
        // 获取今天的开始和结束时间
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        
        // 查询总用户数和今日新增用户数
        long totalUsers = userRepository.count();
        long todayNewUsers = userRepository.countByRegisterTimeBetween(todayStart, todayEnd);
        
        // 查询总电影数
        long totalMovies = movieRepository.count();
        
        // 查询总评论数和待审核评论数
//        long totalComments = commentRepository.count();
//        long pendingComments = commentRepository.countByStatus(0);
        
        // 查询总订单数、今日新增订单数、待支付订单数
        long totalOrders = orderRepository.count();
        long todayNewOrders = orderRepository.countByCreateTimeBetween(todayStart, todayEnd);
        long pendingOrders = orderRepository.countByStatus(0);
        
        // 查询总收入和今日收入
        BigDecimal totalRevenue = orderRepository.sumAmountByStatus(1);
        BigDecimal todayRevenue = orderRepository.sumAmountByStatusAndCreateTimeBetween(1, todayStart, todayEnd);
        
        return DashboardVO.builder()
                .totalUsers(totalUsers)
                .totalMovies(totalMovies)
//                .totalComments(totalComments)
                .totalOrders(totalOrders)
                .pendingOrders(pendingOrders)
//                .pendingComments(pendingComments)
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .todayNewUsers(todayNewUsers)
                .todayNewOrders(todayNewOrders)
                .todayRevenue(todayRevenue != null ? todayRevenue : BigDecimal.ZERO)
                .build();
    }

    @Override
    public Map<String, Object> getOrderStatistics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        
        // 查询时间范围内的订单
        List<Object[]> dailyOrders = orderRepository.countOrdersByDayBetween(startDateTime, endDateTime);
        List<Object[]> dailyRevenue = orderRepository.sumRevenueByDayBetween(startDateTime, endDateTime);
        
        // 使用日期做键，组织数据
        Map<LocalDate, Long> ordersMap = dailyOrders.stream()
                .collect(Collectors.toMap(
                        row -> ((LocalDate) row[0]), 
                        row -> ((Number) row[1]).longValue()));
        
        Map<LocalDate, BigDecimal> revenueMap = dailyRevenue.stream()
                .collect(Collectors.toMap(
                        row -> ((LocalDate) row[0]), 
                        row -> (BigDecimal) row[1]));
        
        // 生成连续日期范围内的数据
        List<String> dates = new ArrayList<>();
        List<Long> orders = new ArrayList<>();
        List<BigDecimal> revenues = new ArrayList<>();
        
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            dates.add(date.toString());
            orders.add(ordersMap.getOrDefault(date, 0L));
            revenues.add(revenueMap.getOrDefault(date, BigDecimal.ZERO));
            date = date.plusDays(1);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("orders", orders);
        result.put("revenues", revenues);
        
        return result;
    }

    @Override
    public Map<String, Object> getUserStatistics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        
        // 查询时间范围内的用户注册
        List<Object[]> dailyUsers = userRepository.countUsersByDayBetween(startDateTime, endDateTime);
        
        // 使用日期做键，组织数据
        Map<LocalDate, Long> usersMap = dailyUsers.stream()
                .collect(Collectors.toMap(
                        row -> ((LocalDate) row[0]), 
                        row -> ((Number) row[1]).longValue()));
        
        // 生成连续日期范围内的数据
        List<String> dates = new ArrayList<>();
        List<Long> users = new ArrayList<>();
        
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            dates.add(date.toString());
            users.add(usersMap.getOrDefault(date, 0L));
            date = date.plusDays(1);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("users", users);
        
        return result;
    }
} 