package com.jerry.ff.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorDTO {
    
    private Long id;
    
    @NotBlank(message = "演员名称不能为空")
    private String name;
    
    private String originalName;
    
    private String avatarUrl;
    
    private String description;
} 