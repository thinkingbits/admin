package com.jerry.ff.controller;

import com.jerry.ff.model.dto.OrderDTO;
import com.jerry.ff.model.vo.OrderVO;
import com.jerry.ff.service.OrderService;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "订单管理", description = "订单相关接口")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "创建订单", description = "创建新订单")
    public ResponseResult<OrderVO> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        return ResponseResult.success(orderService.createOrder(orderDTO, userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @orderService.isOrderOwner(#id, authentication.name)")
    @Operation(summary = "获取订单详情", description = "获取指定订单详情")
    public ResponseResult<OrderVO> getOrder(@PathVariable Long id) {
        return ResponseResult.success(orderService.getOrder(id));
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "获取我的订单", description = "获取当前用户的所有订单")
    public ResponseResult<Page<OrderVO>> getMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseResult.success(orderService.getOrdersByUsername(userDetails.getUsername(), pageable));
    }

    @PutMapping("/{id}/pay")
    @PreAuthorize("@orderService.isOrderOwner(#id, authentication.name)")
    @Operation(summary = "支付订单", description = "支付指定订单")
    public ResponseResult<OrderVO> payOrder(
            @PathVariable Long id,
            @RequestParam String paymentMethod) {
        
        return ResponseResult.success(orderService.payOrder(id, paymentMethod));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @orderService.isOrderOwner(#id, authentication.name)")
    @Operation(summary = "取消订单", description = "取消指定订单")
    public ResponseResult<OrderVO> cancelOrder(@PathVariable Long id) {
        return ResponseResult.success(orderService.cancelOrder(id));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "管理员查询订单", description = "管理员查询所有订单")
    public ResponseResult<Page<OrderVO>> adminGetOrders(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseResult.success(orderService.adminGetOrders(status, startTime, endTime, pageable));
    }
} 