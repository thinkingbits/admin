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
public class CategoryDTO {
    
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    private String description;
    
    private Long parentId;
} 