package com.jerry.ff.service.impl;

import com.jerry.ff.exception.BusinessException;
import com.jerry.ff.exception.ResourceNotFoundException;
import com.jerry.ff.model.dto.OrderDTO;
import com.jerry.ff.model.entity.Order;
import com.jerry.ff.model.entity.Role;
import com.jerry.ff.model.entity.User;
import com.jerry.ff.model.vo.OrderVO;
import com.jerry.ff.repository.OrderRepository;
import com.jerry.ff.repository.RoleRepository;
import com.jerry.ff.repository.UserRepository;
import com.jerry.ff.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public OrderVO createOrder(OrderDTO orderDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在: " + username));
        
        // 生成订单号
        String orderNo = generateOrderNo();
        
        Order order = Order.builder()
                .orderNo(orderNo)
                .user(user)
                .amount(orderDTO.getAmount())
                .status(0) // 未支付状态
                .membershipType(orderDTO.getMembershipType())
                .createTime(LocalDateTime.now())
                .build();
        
        Order savedOrder = orderRepository.save(order);
        return convertToOrderVO(savedOrder);
    }

    @Override
    public OrderVO getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在，ID: " + id));
        
        return convertToOrderVO(order);
    }

    @Override
    public Page<OrderVO> getOrdersByUsername(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在: " + username));
        
        Page<Order> orderPage = orderRepository.findByUserId(user.getId(), pageable);
        return convertToOrderVOPage(orderPage);
    }

    @Override
    @Transactional
    public OrderVO payOrder(Long id, String paymentMethod) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在，ID: " + id));
        
        if (order.getStatus() != 0) {
            throw new BusinessException(400, "订单状态不允许支付");
        }
        
        order.setStatus(1); // 已支付
        order.setPaymentMethod(paymentMethod);
        order.setPayTime(LocalDateTime.now());
        
        Order updatedOrder = orderRepository.save(order);
        
        // 更新用户会员等级
        upgradeUserMembership(order.getUser(), order.getMembershipType());
        
        return convertToOrderVO(updatedOrder);
    }

    @Override
    @Transactional
    public OrderVO cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在，ID: " + id));
        
        if (order.getStatus() != 0) {
            throw new BusinessException(400, "只能取消未支付的订单");
        }
        
        order.setStatus(2); // 已取消
        Order updatedOrder = orderRepository.save(order);
        
        return convertToOrderVO(updatedOrder);
    }

    @Override
    public Page<OrderVO> adminGetOrders(Integer status, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        Page<Order> orderPage;
        
        if (status != null) {
            orderPage = orderRepository.findByStatus(status, pageable);
        } else if (startTime != null && endTime != null) {
            orderPage = orderRepository.findByCreateTimeBetween(startTime, endTime, pageable);
        } else {
            orderPage = orderRepository.findAll(pageable);
        }
        
        return convertToOrderVOPage(orderPage);
    }

    @Override
    public boolean isOrderOwner(Long id, String username) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在，ID: " + id));
        
        return order.getUser().getUsername().equals(username);
    }
    
    private String generateOrderNo() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    
    private void upgradeUserMembership(User user, String membershipType) {
        int memberLevel = 0;
        
        switch (membershipType) {
            case "MONTHLY":
                memberLevel = 1;
                break;
            case "QUARTERLY":
                memberLevel = 2;
                break;
            case "ANNUAL":
                memberLevel = 3;
                break;
            default:
                break;
        }
        
        if (memberLevel > user.getMemberLevel()) {
            user.setMemberLevel(memberLevel);
            
            // 如果是VIP会员，添加VIP角色
            if (memberLevel > 0) {
                Role vipRole = roleRepository.findByName("VIP")
                        .orElseThrow(() -> new BusinessException(500, "系统错误：VIP角色不存在"));
                
                if (user.getRoles().stream().noneMatch(role -> role.getName().equals("VIP"))) {
                    user.getRoles().add(vipRole);
                }
            }
            
            userRepository.save(user);
        }
    }

    private OrderVO convertToOrderVO(Order order) {
        String statusDesc;
        switch (order.getStatus()) {
            case 0:
                statusDesc = "未支付";
                break;
            case 1:
                statusDesc = "已支付";
                break;
            case 2:
                statusDesc = "已取消";
                break;
            default:
                statusDesc = "未知状态";
        }
        
        return OrderVO.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .userId(order.getUser().getId())
                .username(order.getUser().getUsername())
                .amount(order.getAmount())
                .status(order.getStatus())
                .statusDesc(statusDesc)
                .paymentMethod(order.getPaymentMethod())
                .membershipType(order.getMembershipType())
                .createTime(order.getCreateTime())
                .payTime(order.getPayTime())
                .build();
    }
    
    private Page<OrderVO> convertToOrderVOPage(Page<Order> orderPage) {
        List<OrderVO> orderVOs = orderPage.getContent().stream()
                .map(this::convertToOrderVO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(orderVOs, orderPage.getPageable(), orderPage.getTotalElements());
    }
} 