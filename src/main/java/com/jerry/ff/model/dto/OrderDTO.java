package com.jerry.ff.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    
    @NotNull(message = "订单金额不能为空")
    @Positive(message = "订单金额必须为正数")
    private BigDecimal amount;
    
    @NotBlank(message = "会员类型不能为空")
    private String membershipType;
} 