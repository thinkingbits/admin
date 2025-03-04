package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private Integer status;
    private Integer memberLevel;
    private List<String> roles;
    private LocalDateTime registerTime;
    private LocalDateTime lastLoginTime;
} 