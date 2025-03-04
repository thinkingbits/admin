package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse implements Serializable {
    
    private String token;
    private String tokenType;
    private Long id;
    private String username;
    private String email;
    private String avatar;
    private List<String> roles;
    private Integer memberLevel;
} 