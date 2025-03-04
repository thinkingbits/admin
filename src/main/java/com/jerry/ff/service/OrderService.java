package com.jerry.ff.service;

import com.jerry.ff.model.dto.OrderDTO;
import com.jerry.ff.model.vo.OrderVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface OrderService {
    
    OrderVO createOrder(OrderDTO orderDTO, String username);
    
    OrderVO getOrder(Long id);
    
    Page<OrderVO> getOrdersByUsername(String username, Pageable pageable);
    
    OrderVO payOrder(Long id, String paymentMethod);
    
    OrderVO cancelOrder(Long id);
    
    Page<OrderVO> adminGetOrders(Integer status, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    boolean isOrderOwner(Long id, String username);
} 