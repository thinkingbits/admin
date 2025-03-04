package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO implements Serializable {
    
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private BigDecimal amount;
    private Integer status; // 0-未支付, 1-已支付, 2-已取消
    private String statusDesc;
    private String paymentMethod;
    private String membershipType;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
} 